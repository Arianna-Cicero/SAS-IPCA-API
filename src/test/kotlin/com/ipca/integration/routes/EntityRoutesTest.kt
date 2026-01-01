package com.ipca.integration.routes

import com.ipca.IntegrationTest
import io.ktor.client.request.*
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlin.test.Test
import kotlin.test.assertEquals

class EntityRoutesTest : IntegrationTest() {

    @Test
    fun `should list all entities`() = testApp {
        val response = client.get("/entities")
        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun `should create entity`() = testApp {
        val response = client.post("/entities") {
            contentType(ContentType.Application.Json)
            setBody("""
                {
                  "name": "TestEntity"
                }
            """)
        }
        assertEquals(HttpStatusCode.Created, response.status)
    }

    @Test
    fun `should get entity by id`() = testApp {
        val response = client.get("/entities/1")
        assertEquals(true, response.status in listOf(HttpStatusCode.OK, HttpStatusCode.NotFound))
    }

    @Test
    fun `should get entity by name`() = testApp {
        val response = client.get("/entities/by-name/TestEntity")
        assertEquals(true, response.status in listOf(HttpStatusCode.OK, HttpStatusCode.NotFound))
    }
}
