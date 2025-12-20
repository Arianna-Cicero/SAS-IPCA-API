package com.ipca.routes

import com.ipca.dto.Collaborator.CollaboratorLoginResponseDTO
import com.ipca.services.AuthService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(val email: String, val password: String)

fun Route.authRoutes() {

    post("/auth/login") {
        try {
            val request = call.receive<LoginRequest>()

            val session = AuthService.login(request.email, request.password)
            call.respond(HttpStatusCode.OK, session)
        } catch (e: Exception) {
            call.respondText("Invalid login credentials", status = HttpStatusCode.Unauthorized)
        }
    }

    post("/auth/logout") {
        try {
            val authHeader = call.request.headers["Authorization"]
                ?: return@post call.respondText("Missing authorization header", status = HttpStatusCode.BadRequest)

            val token = authHeader.removePrefix("Bearer ")
            AuthService.logout(token)
            call.respond(HttpStatusCode.OK, mapOf("message" to "Logged out successfully"))
        } catch (e: Exception) {
            call.respondText("Error: ${e.message}", status = HttpStatusCode.InternalServerError)
        }
    }

    post("/auth/validate") {
        try {
            val authHeader = call.request.headers["Authorization"]
                ?: return@post call.respondText("Missing authorization header", status = HttpStatusCode.BadRequest)

            val token = authHeader.removePrefix("Bearer ")
            val isValid = AuthService.validateSession(token)
            call.respond(HttpStatusCode.OK, mapOf("valid" to isValid))
        } catch (e: Exception) {
            call.respondText("Error: ${e.message}", status = HttpStatusCode.InternalServerError)
        }
    }
}
