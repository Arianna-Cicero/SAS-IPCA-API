package com.ipca.integration.routes

import com.ipca.IntegrationTest
import io.ktor.client.request.*
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlin.test.Test
import kotlin.test.assertEquals

class DeliveryItemRoutesTest : IntegrationTest() {

    @Test
    fun `should list all delivery items`() = testApp {
        val response = client.get("/delivery-items")
        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun `should create delivery item`() = testApp {
        val response = client.post("/delivery-items") {
            contentType(ContentType.Application.Json)
            setBody("""
                {
                  "goodId": 1,
                  "quantity": 10
                }
            """)
        }
        assertEquals(HttpStatusCode.Created, response.status)
    }

    @Test
    fun `should get items by delivery id`() = testApp {
        val response = client.get("/delivery-items/by-delivery/550e8400-e29b-41d4-a716-446655440000")
        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun `should delete delivery item`() = testApp {
        val response = client.delete("/delivery-items/550e8400-e29b-41d4-a716-446655440000/1")
        assertEquals(true, response.status in listOf(HttpStatusCode.OK, HttpStatusCode.NotFound))
    }
}
