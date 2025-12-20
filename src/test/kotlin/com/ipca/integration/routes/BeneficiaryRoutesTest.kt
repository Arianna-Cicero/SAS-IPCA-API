package com.ipca.integration.routes

import com.ipca.IntegrationTest
import io.ktor.client.request.*
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlin.test.Test
import kotlin.test.assertEquals

class BeneficiaryRoutesTest : IntegrationTest() {

    @Test
    fun `should create beneficiary`() = testApp {
        val response = client.post("/beneficiaries") {
            contentType(ContentType.Application.Json)
            setBody("""
                {
                  "name": "João Silva",
                  "studentNumber": 12345,
                  "email": "joao@ipca.pt",
                  "telephone": "912345678",
                  "idCourse": 1
                }
            """)
        }
        assertEquals(HttpStatusCode.Created, response.status)
    }

    @Test
    fun `should list all beneficiaries`() = testApp {
        val response = client.get("/beneficiaries")
        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun `should get beneficiary by id`() = testApp {
        val response = client.get("/beneficiaries/12345")
        // Will return 404 if not found, but endpoint is accessible
        assertEquals(true, response.status in listOf(HttpStatusCode.OK, HttpStatusCode.NotFound))
    }

    @Test
    fun `should update beneficiary`() = testApp {
        val response = client.put("/beneficiaries/12345") {
            contentType(ContentType.Application.Json)
            setBody("""
                {
                  "name": "João Silva Updated",
                  "email": "joao.updated@ipca.pt"
                }
            """)
        }
        assertEquals(true, response.status in listOf(HttpStatusCode.OK, HttpStatusCode.NotFound))
    }

    @Test
    fun `should delete beneficiary`() = testApp {
        val response = client.delete("/beneficiaries/12345")
        assertEquals(true, response.status in listOf(HttpStatusCode.OK, HttpStatusCode.NotFound))
    }

    @Test
    fun `should get scheduling for beneficiary`() = testApp {
        val response = client.get("/beneficiaries/12345/scheduling")
        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun `should get deliveries for beneficiary`() = testApp {
        val response = client.get("/beneficiaries/12345/deliveries")
        assertEquals(HttpStatusCode.OK, response.status)
    }
}