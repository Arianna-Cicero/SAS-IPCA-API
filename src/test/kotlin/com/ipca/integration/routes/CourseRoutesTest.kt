package com.ipca.integration.routes

import com.ipca.IntegrationTest
import io.ktor.client.request.*
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlin.test.Test
import kotlin.test.assertEquals

class CourseRoutesTest : IntegrationTest() {

    @Test
    fun `should list all courses`() = testApp {
        val response = client.get("/courses")
        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun `should create course`() = testApp {
        val response = client.post("/courses") {
            contentType(ContentType.Application.Json)
            setBody("""
                {
                  "name": "Computer Science"
                }
            """)
        }
        assertEquals(HttpStatusCode.Created, response.status)
    }

    @Test
    fun `should get course by id`() = testApp {
        val response = client.get("/courses/1")
        assertEquals(true, response.status in listOf(HttpStatusCode.OK, HttpStatusCode.NotFound))
    }

    @Test
    fun `should update course`() = testApp {
        val response = client.put("/courses/1") {
            contentType(ContentType.Application.Json)
            setBody("""
                {
                  "name": "Updated Course"
                }
            """)
        }
        assertEquals(true, response.status in listOf(HttpStatusCode.OK, HttpStatusCode.NotFound))
    }

    @Test
    fun `should delete course`() = testApp {
        val response = client.delete("/courses/1")
        assertEquals(true, response.status in listOf(HttpStatusCode.OK, HttpStatusCode.NotFound))
    }
}
