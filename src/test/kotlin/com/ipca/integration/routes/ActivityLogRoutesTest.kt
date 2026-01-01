package com.ipca.integration.routes

import com.ipca.IntegrationTest
import io.ktor.client.request.*
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlin.test.Test
import kotlin.test.assertEquals

class ActivityLogRoutesTest : IntegrationTest() {

    @Test
    fun `should list all activity logs`() = testApp {
        val response = client.get("/activity-logs")
        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun `should create activity log`() = testApp {
        val response = client.post("/activity-logs") {
            contentType(ContentType.Application.Json)
            setBody("""
                {
                  "collaboratorId": "550e8400-e29b-41d4-a716-446655440000",
                  "entityId": 1,
                  "recordId": "123",
                  "action": "CREATE"
                }
            """)
        }
        assertEquals(HttpStatusCode.Created, response.status)
    }

    @Test
    fun `should get activity log by id`() = testApp {
        val response = client.get("/activity-logs/1")
        assertEquals(true, response.status in listOf(HttpStatusCode.OK, HttpStatusCode.NotFound))
    }

    @Test
    fun `should get logs by collaborator id`() = testApp {
        val response = client.get("/activity-logs/collaborator/550e8400-e29b-41d4-a716-446655440000")
        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun `should get logs by entity id`() = testApp {
        val response = client.get("/activity-logs/entity/1")
        assertEquals(HttpStatusCode.OK, response.status)
    }
}
