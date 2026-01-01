package com.ipca.integration.routes

import com.ipca.IntegrationTest
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlin.test.Test
import kotlin.test.assertEquals

class AuthRoutesTest : IntegrationTest() {

    @Test
    fun testHealthCheckEndpoint() = testApp {
        val response = client.get("/")
        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun testLoginWithInvalidCredentials() = testApp {
        val response = client.post("/auth/login") {
            contentType(ContentType.Application.Json)
            setBody("""
                {
                  "email": "nonexistent@ipca.pt",
                  "password": "wrongpassword"
                }
            """)
        }
        assertEquals(HttpStatusCode.Unauthorized, response.status)
    }

    @Test
    fun testLogoutWithoutToken() = testApp {
        val response = client.post("/auth/logout")
        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

    @Test
    fun testValidateWithoutToken() = testApp {
        val response = client.post("/auth/validate")
        assertEquals(HttpStatusCode.BadRequest, response.status)
    }
}
