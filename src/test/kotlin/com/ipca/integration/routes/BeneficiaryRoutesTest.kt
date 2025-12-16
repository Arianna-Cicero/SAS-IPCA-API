package com.ipca.integration.routes

import com.ipca.IntegrationTest
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlin.test.Test
import kotlin.test.assertEquals

class BeneficiaryRoutesTest: IntegrationTest() {
    @Test
    fun `should create beneficiary`() = testApp {

        val response = client.post("/beneficiaries") {
            contentType(ContentType.Application.Json)
            setBody("""
                {
                  "name": "João",
                  "studentNumber": 12345,
                  "email": "joao@ipca.pt",
                  "telephone": "912345678",
                  "courseId": 1
                }
            """)
        }

        assertEquals(HttpStatusCode.Created, response.status)
    }
}