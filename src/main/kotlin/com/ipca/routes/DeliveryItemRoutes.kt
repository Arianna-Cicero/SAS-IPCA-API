package com.ipca.routes

import com.ipca.dto.DeliveryItem.DeliveryItemCreateDTO
import com.ipca.services.DeliveryItemService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.server.routing.*

fun Route.deliveryItemRoutes() {
    route("/delivery-items") {
        // GET /delivery-items → list all delivery items
        get {
            try {
                val items = DeliveryItemService.getAll()
                call.respond(items)
            } catch (e: Exception) {
                call.respondText("Error: ${e.message}", status = HttpStatusCode.InternalServerError)
            }
        }

        // POST /delivery-items → create delivery item
        post {
            try {
                val request = call.receive<DeliveryItemCreateDTO>()
                DeliveryItemService.create(request)
                call.respond(HttpStatusCode.Created, mapOf("message" to "Delivery item created"))
            } catch (e: Exception) {
                call.respondText("Error: ${e.message}", status = HttpStatusCode.BadRequest)
            }
        }

        // GET /delivery-items/by-delivery/{deliveryId} → get items for a delivery
        get("by-delivery/{deliveryId}") {
            try {
                val deliveryId = call.parameters["deliveryId"]
                    ?: return@get call.respondText("Invalid delivery ID", status = HttpStatusCode.BadRequest)

                val items = DeliveryItemService.getByDeliveryId(deliveryId)
                call.respond(items)
            } catch (e: Exception) {
                call.respondText("Error: ${e.message}", status = HttpStatusCode.InternalServerError)
            }
        }

        // DELETE /delivery-items/{deliveryId}/{goodId} → delete delivery item
        delete("{deliveryId}/{goodId}") {
            try {
                val deliveryId = call.parameters["deliveryId"]
                    ?: return@delete call.respondText("Invalid delivery ID", status = HttpStatusCode.BadRequest)
                val goodId = call.parameters["goodId"]?.toIntOrNull()
                    ?: return@delete call.respondText("Invalid good ID", status = HttpStatusCode.BadRequest)

                DeliveryItemService.delete(deliveryId, goodId)
                call.respond(mapOf("message" to "Delivery item deleted"))
            } catch (e: Exception) {
                call.respondText("Error: ${e.message}", status = HttpStatusCode.InternalServerError)
            }
        }
    }
}