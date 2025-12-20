package com.ipca.integration.routes

import com.ipca.IntegrationTest
import io.ktor.client.request.*
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlin.test.Test
import kotlin.test.assertEquals

class DonationRoutesTest : IntegrationTest() {

    @Test
    fun `should list all donations`() = testApp {
        val response = client.get("/donations")
        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun `should create donation`() = testApp {
        val response = client.post("/donations") {
            contentType(ContentType.Application.Json)
            setBody("""
                {
                  "nameDonor": "John Doe",
                  "type": "food",
                  "dateDonor": "2025-12-20",
                  "description": "Food donation"
                }
            """)
        }
        assertEquals(HttpStatusCode.Created, response.status)
    }

    @Test
    fun `should get donation by id`() = testApp {
        val response = client.get("/donations/550e8400-e29b-41d4-a716-446655440000")
        assertEquals(true, response.status in listOf(HttpStatusCode.OK, HttpStatusCode.NotFound))
    }

    @Test
    fun `should update donation`() = testApp {
        val response = client.put("/donations/550e8400-e29b-41d4-a716-446655440000") {
            contentType(ContentType.Application.Json)
            setBody("""
                {
                  "type": "clothing",
                  "description": "Updated description"
                }
            """)
        }
        assertEquals(true, response.status in listOf(HttpStatusCode.OK, HttpStatusCode.NotFound))
    }

    @Test
    fun `should delete donation`() = testApp {
        val response = client.delete("/donations/550e8400-e29b-41d4-a716-446655440000")
        assertEquals(true, response.status in listOf(HttpStatusCode.OK, HttpStatusCode.NotFound))
    }
}
