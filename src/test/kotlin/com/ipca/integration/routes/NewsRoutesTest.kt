package com.ipca.integration.routes

import com.ipca.IntegrationTest
import io.ktor.client.request.*
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlin.test.Test
import kotlin.test.assertEquals

class NewsRoutesTest : IntegrationTest() {

    @Test
    fun `should list all news`() = testApp {
        val response = client.get("/news")
        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun `should create news`() = testApp {
        val response = client.post("/news") {
            contentType(ContentType.Application.Json)
            setBody("""
                {
                  "title": "New Campaign",
                  "content": "This is news content",
                  "datePublication": "2025-12-20",
                  "campaignId": 1
                }
            """)
        }
        assertEquals(HttpStatusCode.Created, response.status)
    }

    @Test
    fun `should get news by id`() = testApp {
        val response = client.get("/news/1")
        assertEquals(true, response.status in listOf(HttpStatusCode.OK, HttpStatusCode.NotFound))
    }

    @Test
    fun `should update news`() = testApp {
        val response = client.put("/news/1") {
            contentType(ContentType.Application.Json)
            setBody("""
                {
                  "title": "Updated News",
                  "content": "Updated content"
                }
            """)
        }
        assertEquals(true, response.status in listOf(HttpStatusCode.OK, HttpStatusCode.NotFound))
    }

    @Test
    fun `should delete news`() = testApp {
        val response = client.delete("/news/1")
        assertEquals(true, response.status in listOf(HttpStatusCode.OK, HttpStatusCode.NotFound))
    }

    @Test
    fun `should get news by campaign`() = testApp {
        val response = client.get("/news/campaign/1")
        assertEquals(HttpStatusCode.OK, response.status)
    }
}
