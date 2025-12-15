package com.ipca.routes

import com.ipca.services.AuthService
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(val email: String, val password: String)

fun Route.authRoutes() {

    post("/auth/login") {
        val request = call.receive<LoginRequest>()

        try {
            val session = AuthService.login(request.email, request.password)
            call.respond(session)
        } catch (e: Exception) {
            call.respondText("Invalid login", status = io.ktor.http.HttpStatusCode.Unauthorized)
        }
    }
}
