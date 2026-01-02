package com.ipca.integration

import com.ipca.TestApplicationFactory
import com.ipca.auth.JwtProvider
import com.ipca.auth.PasswordUtils
import com.ipca.exceptions.ErrorResponse
import com.ipca.models.CollaboratorTable
import com.ipca.services.LoginResponse
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Test
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class AuthenticationIntegrationTest {

    @Test
    fun testLoginSuccess() = TestApplicationFactory.createTestClient { client ->
        // Setup test collaborator
        val email = "test@example.com"
        val password = "securePassword123"
        val hashedPassword = PasswordUtils.hash(password)
        
        transaction {
            CollaboratorTable.insert {
                it[id_collaborator] = UUID.randomUUID()
                it[name] = "Test User"
                it[this.email] = email
                it[this.password] = hashedPassword
            }
        }

        // Test login
        val response = client.post("/auth/login") {
            contentType(ContentType.Application.Json)
            setBody("""{"email":"$email","password":"$password"}""")
        }

        assertEquals(HttpStatusCode.OK, response.status)
        val loginResponse: LoginResponse = response.body()
        assertNotNull(loginResponse.token)
        assertEquals(email, loginResponse.email)
    }

    @Test
    fun testLoginInvalidCredentials() = TestApplicationFactory.createTestClient { client ->
        val response = client.post("/auth/login") {
            contentType(ContentType.Application.Json)
            setBody("""{"email":"nonexistent@example.com","password":"wrongpassword"}""")
        }

        assertEquals(HttpStatusCode.Unauthorized, response.status)
        val errorResponse: ErrorResponse = response.body()
        assertEquals("UNAUTHORIZED", errorResponse.code)
    }

    @Test
    fun testAccessProtectedRouteWithoutToken() = TestApplicationFactory.createTestClient { client ->
        val response = client.get("/collaborators")
        
        assertEquals(HttpStatusCode.Unauthorized, response.status)
    }

    @Test
    fun testAccessProtectedRouteWithValidToken() = TestApplicationFactory.createTestClient { client ->
        // Setup test collaborator
        val collaboratorId = UUID.randomUUID()
        val email = "test@example.com"
        val password = "securePassword123"
        val hashedPassword = PasswordUtils.hash(password)
        
        transaction {
            CollaboratorTable.insert {
                it[id_collaborator] = collaboratorId
                it[name] = "Test User"
                it[this.email] = email
                it[this.password] = hashedPassword
            }
        }

        // Login to get token
        val loginResponse = client.post("/auth/login") {
            contentType(ContentType.Application.Json)
            setBody("""{"email":"$email","password":"$password"}""")
        }
        
        val token = loginResponse.body<LoginResponse>().token

        // Access protected route
        val response = client.get("/collaborators") {
            header(HttpHeaders.Authorization, "Bearer $token")
        }

        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun testAccessProtectedRouteWithInvalidToken() = TestApplicationFactory.createTestClient { client ->
        val response = client.get("/collaborators") {
            header(HttpHeaders.Authorization, "Bearer invalid.token.here")
        }

        assertEquals(HttpStatusCode.Unauthorized, response.status)
    }
}
