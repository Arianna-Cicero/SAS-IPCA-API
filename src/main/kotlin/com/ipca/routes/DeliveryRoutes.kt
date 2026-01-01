package com.ipca.routes

import com.ipca.dto.Delivery.DeliveryCreateDTO
import com.ipca.dto.Delivery.DeliveryUpdateStatusDTO
import com.ipca.services.DeliveryService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.server.routing.*

fun Route.deliveryRoutes() {
    route("/deliveries") {
        // GET /deliveries → list all deliveries
        get {
            try {
                val deliveries = DeliveryService.getAll()
                call.respond(deliveries)
            } catch (e: Exception) {
                call.respondText("Error: ${e.message}", status = HttpStatusCode.InternalServerError)
            }
        }

        // POST /deliveries → create delivery
        post {
            try {
                val request = call.receive<DeliveryCreateDTO>()
                val id = DeliveryService.create(request)
                call.respond(HttpStatusCode.Created, mapOf("message" to "Delivery created", "id" to id))
            } catch (e: Exception) {
                call.respondText("Error: ${e.message}", status = HttpStatusCode.BadRequest)
            }
        }

        // GET /deliveries/{id} → get single delivery
        get("{id}") {
            try {
                val id = call.parameters["id"]
                    ?: return@get call.respondText("Invalid ID", status = HttpStatusCode.BadRequest)

                val delivery = DeliveryService.getById(id)
                    ?: return@get call.respondText("Delivery not found", status = HttpStatusCode.NotFound)

                call.respond(delivery)
            } catch (e: Exception) {
                call.respondText("Error: ${e.message}", status = HttpStatusCode.InternalServerError)
            }
        }

        // PUT /deliveries/{id}/status → update delivery status
        put("{id}/status") {
            try {
                val id = call.parameters["id"]
                    ?: return@put call.respondText("Invalid ID", status = HttpStatusCode.BadRequest)

                val request = call.receive<DeliveryUpdateStatusDTO>()
                DeliveryService.updateStatus(id, request.status)
                call.respond(mapOf("message" to "Delivery status updated"))
            } catch (e: Exception) {
                call.respondText("Error: ${e.message}", status = HttpStatusCode.BadRequest)
            }
        }

        // DELETE /deliveries/{id} → delete delivery
        delete("{id}") {
            try {
                val id = call.parameters["id"]
                    ?: return@delete call.respondText("Invalid ID", status = HttpStatusCode.BadRequest)

                DeliveryService.delete(id)
                call.respond(mapOf("message" to "Delivery deleted"))
            } catch (e: Exception) {
                call.respondText("Error: ${e.message}", status = HttpStatusCode.InternalServerError)
            }
        }
    }
}