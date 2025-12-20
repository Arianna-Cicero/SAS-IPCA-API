package com.ipca.integration.routes

import com.ipca.IntegrationTest
import io.ktor.client.request.*
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlin.test.Test
import kotlin.test.assertEquals

class SchedulingRoutesTest : IntegrationTest() {

    @Test
    fun `should list all schedulings`() = testApp {
        val response = client.get("/scheduling")
        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun `should create scheduling`() = testApp {
        val response = client.post("/scheduling") {
            contentType(ContentType.Application.Json)
            setBody("""
                {
                  "beneficiaryId": 12345,
                  "collaboratorId": "550e8400-e29b-41d4-a716-446655440000",
                  "dateDelivery": "2025-12-25"
                }
            """)
        }
        assertEquals(HttpStatusCode.Created, response.status)
    }

    @Test
    fun `should get scheduling by id`() = testApp {
        val response = client.get("/scheduling/1")
        assertEquals(true, response.status in listOf(HttpStatusCode.OK, HttpStatusCode.NotFound))
    }

    @Test
    fun `should update scheduling`() = testApp {
        val response = client.put("/scheduling/1") {
            contentType(ContentType.Application.Json)
            setBody("""
                {
                  "dateDelivery": "2025-12-26",
                  "status": "in-progress"
                }
            """)
        }
        assertEquals(true, response.status in listOf(HttpStatusCode.OK, HttpStatusCode.NotFound))
    }

    @Test
    fun `should delete scheduling`() = testApp {
        val response = client.delete("/scheduling/1")
        assertEquals(true, response.status in listOf(HttpStatusCode.OK, HttpStatusCode.NotFound))
    }
}
