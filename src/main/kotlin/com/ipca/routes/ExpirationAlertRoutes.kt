package com.ipca.routes

import com.ipca.dto.common.CreateResponseDTO
import com.ipca.dto.ExpirationAlert.ExpirationAlertCreateDTO
import com.ipca.dto.ExpirationAlert.ExpirationAlertResolveDTO
import com.ipca.services.ExpirationAlertService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.server.routing.*

fun Route.expirationAlertRoutes() {
    route("/expiration-alerts") {
        // GET /expiration-alerts → list all alerts
        get {
            try {
                val alerts = ExpirationAlertService.getAll()
                call.respond(alerts)
            } catch (e: Exception) {
                call.respondText("Error: ${e.message}", status = HttpStatusCode.InternalServerError)
            }
        }

        // POST /expiration-alerts → create alert
        post {
            try {
                val request = call.receive<ExpirationAlertCreateDTO>()
                val id = ExpirationAlertService.create(request)
                call.respond(HttpStatusCode.Created, CreateResponseDTO("Alert created", id.toString()))
            } catch (e: Exception) {
                call.respondText("Error: ${e.message}", status = HttpStatusCode.BadRequest)
            }
        }

        // GET /expiration-alerts/{id} → get single alert
        get("{id}") {
            try {
                val id = call.parameters["id"]?.toIntOrNull()
                    ?: return@get call.respondText("Invalid ID", status = HttpStatusCode.BadRequest)

                val alert = ExpirationAlertService.getById(id)
                    ?: return@get call.respondText("Alert not found", status = HttpStatusCode.NotFound)

                call.respond(alert)
            } catch (e: Exception) {
                call.respondText("Error: ${e.message}", status = HttpStatusCode.InternalServerError)
            }
        }

        // PUT /expiration-alerts/{id}/resolve → resolve alert
        put("{id}/resolve") {
            try {
                val id = call.parameters["id"]?.toIntOrNull()
                    ?: return@put call.respondText("Invalid ID", status = HttpStatusCode.BadRequest)

                val request = call.receive<ExpirationAlertResolveDTO>()
                ExpirationAlertService.resolve(id, request.resolved)
                call.respond(mapOf("message" to "Alert resolved"))
            } catch (e: Exception) {
                call.respondText("Error: ${e.message}", status = HttpStatusCode.BadRequest)
            }
        }

        // DELETE /expiration-alerts/{id} → delete alert
        delete("{id}") {
            try {
                val id = call.parameters["id"]?.toIntOrNull()
                    ?: return@delete call.respondText("Invalid ID", status = HttpStatusCode.BadRequest)

                ExpirationAlertService.delete(id)
                call.respond(mapOf("message" to "Alert deleted"))
            } catch (e: Exception) {
                call.respondText("Error: ${e.message}", status = HttpStatusCode.InternalServerError)
            }
        }

        // GET /expiration-alerts/active → list unresolved alerts
        get("active/all") {
            try {
                val alerts = ExpirationAlertService.getUnresolved()
                call.respond(alerts)
            } catch (e: Exception) {
                call.respondText("Error: ${e.message}", status = HttpStatusCode.InternalServerError)
            }
        }
    }
}
