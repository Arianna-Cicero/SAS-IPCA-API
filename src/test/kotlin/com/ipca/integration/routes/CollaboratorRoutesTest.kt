package com.ipca.integration.routes

import com.ipca.IntegrationTest
import io.ktor.client.request.*
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlin.test.Test
import kotlin.test.assertEquals

class CollaboratorRoutesTest : IntegrationTest() {

    @Test
    fun `should list all collaborators`() = testApp {
        val response = client.get("/collaborators")
        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun `should create collaborator`() = testApp {
        val response = client.post("/collaborators") {
            contentType(ContentType.Application.Json)
            setBody("""
                {
                  "name": "Maria Silva",
                  "email": "maria@ipca.pt",
                  "password": "securePassword123",
                  "profile": "admin"
                }
            """)
        }
        assertEquals(HttpStatusCode.Created, response.status)
    }

    @Test
    fun `should get collaborator by id`() = testApp {
        val response = client.get("/collaborators/550e8400-e29b-41d4-a716-446655440000")
        assertEquals(true, response.status in listOf(HttpStatusCode.OK, HttpStatusCode.NotFound))
    }

    @Test
    fun `should update collaborator`() = testApp {
        val response = client.put("/collaborators/550e8400-e29b-41d4-a716-446655440000") {
            contentType(ContentType.Application.Json)
            setBody("""
                {
                  "name": "Maria Updated",
                  "profile": "user"
                }
            """)
        }
        assertEquals(true, response.status in listOf(HttpStatusCode.OK, HttpStatusCode.NotFound))
    }

    @Test
    fun `should delete collaborator`() = testApp {
        val response = client.delete("/collaborators/550e8400-e29b-41d4-a716-446655440000")
        assertEquals(true, response.status in listOf(HttpStatusCode.OK, HttpStatusCode.NotFound))
    }
}
