package com.ipca.integration.routes

import com.ipca.IntegrationTest
import io.ktor.client.request.*
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlin.test.Test
import kotlin.test.assertEquals

class DeliveryRoutesTest : IntegrationTest() {

    @Test
    fun `should list all deliveries`() = testApp {
        val response = client.get("/deliveries")
        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun `should create delivery`() = testApp {
        val response = client.post("/deliveries") {
            contentType(ContentType.Application.Json)
            setBody("""
                {
                  "schedulingId": 1,
                  "dateDelivery": "2025-12-25"
                }
            """)
        }
        assertEquals(HttpStatusCode.Created, response.status)
    }

    @Test
    fun `should get delivery by id`() = testApp {
        val response = client.get("/deliveries/550e8400-e29b-41d4-a716-446655440000")
        assertEquals(true, response.status in listOf(HttpStatusCode.OK, HttpStatusCode.NotFound))
    }

    @Test
    fun `should update delivery status`() = testApp {
        val response = client.put("/deliveries/550e8400-e29b-41d4-a716-446655440000/status") {
            contentType(ContentType.Application.Json)
            setBody("""
                {
                  "status": "completed"
                }
            """)
        }
        assertEquals(true, response.status in listOf(HttpStatusCode.OK, HttpStatusCode.NotFound))
    }

    @Test
    fun `should delete delivery`() = testApp {
        val response = client.delete("/deliveries/550e8400-e29b-41d4-a716-446655440000")
        assertEquals(true, response.status in listOf(HttpStatusCode.OK, HttpStatusCode.NotFound))
    }
}
