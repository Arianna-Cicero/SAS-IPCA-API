package com.ipca.integration.routes

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals

class AuthRoutesTest {

    @Test
    fun testHealthCheckEndpoint() = testApplication {
        val response = client.get("/")
        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun testUnauthorizedAccess() = testApplication {
        val response = client.get("/protected-route")
        assertEquals(HttpStatusCode.Unauthorized, response.status)
    }

    // Add more test cases as needed
}
