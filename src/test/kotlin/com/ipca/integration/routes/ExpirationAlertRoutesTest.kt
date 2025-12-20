package com.ipca.integration.routes

import com.ipca.IntegrationTest
import io.ktor.client.request.*
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlin.test.Test
import kotlin.test.assertEquals

class ExpirationAlertRoutesTest : IntegrationTest() {

    @Test
    fun `should list all alerts`() = testApp {
        val response = client.get("/expiration-alerts")
        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun `should create alert`() = testApp {
        val response = client.post("/expiration-alerts") {
            contentType(ContentType.Application.Json)
            setBody("""
                {
                  "goodId": 1,
                  "dateAlert": "2025-12-25",
                  "remainingDays": 5
                }
            """)
        }
        assertEquals(HttpStatusCode.Created, response.status)
    }

    @Test
    fun `should get alert by id`() = testApp {
        val response = client.get("/expiration-alerts/1")
        assertEquals(true, response.status in listOf(HttpStatusCode.OK, HttpStatusCode.NotFound))
    }

    @Test
    fun `should resolve alert`() = testApp {
        val response = client.put("/expiration-alerts/1/resolve") {
            contentType(ContentType.Application.Json)
            setBody("""
                {
                  "resolved": true
                }
            """)
        }
        assertEquals(true, response.status in listOf(HttpStatusCode.OK, HttpStatusCode.NotFound))
    }

    @Test
    fun `should delete alert`() = testApp {
        val response = client.delete("/expiration-alerts/1")
        assertEquals(true, response.status in listOf(HttpStatusCode.OK, HttpStatusCode.NotFound))
    }

    @Test
    fun `should get unresolved alerts`() = testApp {
        val response = client.get("/expiration-alerts/active/all")
        assertEquals(HttpStatusCode.OK, response.status)
    }
}
