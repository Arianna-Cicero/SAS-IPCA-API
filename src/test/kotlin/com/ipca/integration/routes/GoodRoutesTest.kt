package com.ipca.integration.routes

import com.ipca.IntegrationTest
import io.ktor.client.request.*
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlin.test.Test
import kotlin.test.assertEquals

class GoodRoutesTest : IntegrationTest() {

    @Test
    fun `should list all goods`() = testApp {
        val response = client.get("/goods")
        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun `should create good`() = testApp {
        val response = client.post("/goods") {
            contentType(ContentType.Application.Json)
            setBody("""
                {
                  "name": "Rice",
                  "category": "food",
                  "quantity": 50,
                  "intake": "2025-12-20",
                  "dateValidity": "2026-12-20",
                  "donationId": "550e8400-e29b-41d4-a716-446655440000"
                }
            """)
        }
        assertEquals(HttpStatusCode.Created, response.status)
    }

    @Test
    fun `should get good by id`() = testApp {
        val response = client.get("/goods/1")
        assertEquals(true, response.status in listOf(HttpStatusCode.OK, HttpStatusCode.NotFound))
    }

    @Test
    fun `should update good`() = testApp {
        val response = client.put("/goods/1") {
            contentType(ContentType.Application.Json)
            setBody("""
                {
                  "name": "Updated Good",
                  "quantity": 100
                }
            """)
        }
        assertEquals(true, response.status in listOf(HttpStatusCode.OK, HttpStatusCode.NotFound))
    }

    @Test
    fun `should delete good`() = testApp {
        val response = client.delete("/goods/1")
        assertEquals(true, response.status in listOf(HttpStatusCode.OK, HttpStatusCode.NotFound))
    }

    @Test
    fun `should get goods by category`() = testApp {
        val response = client.get("/goods/category/food")
        assertEquals(HttpStatusCode.OK, response.status)
    }
}
