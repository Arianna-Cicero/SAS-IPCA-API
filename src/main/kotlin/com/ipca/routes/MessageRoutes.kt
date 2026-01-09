package com.ipca.routes

import com.ipca.dto.Message.MessageCreateDTO
import com.ipca.dto.common.CreateResponseDTO
import com.ipca.services.MessageService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.server.routing.*

fun Route.messageRoutes() {
    route("/messages") {

        // GET /messages → list all messages
        get {
            try {
                val messages = MessageService.getAllMessages()
                call.respond(HttpStatusCode.OK, messages)
            } catch (e: Exception) {
                call.respondText("Error: ${e.message}", status = HttpStatusCode.InternalServerError)
            }
        }

        // POST /messages → create message (público, sem autenticação)
        post {
            try {
                val request = call.receive<MessageCreateDTO>()
                val id = MessageService.addMessage(
                    name = request.name,
                    email = request.email,
                    category = request.category,
                    message = request.message
                )
                call.respond(HttpStatusCode.Created, CreateResponseDTO("Message created successfully", id.toString()))
            } catch (e: Exception) {
                call.respondText("Error: ${e.message}", status = HttpStatusCode.BadRequest)
            }
        }

        // GET /messages/{id} → get single message
        get("{id}") {
            try {
                val id = call.parameters["id"]?.toIntOrNull()
                    ?: return@get call.respondText("Invalid ID", status = HttpStatusCode.BadRequest)

                val message = MessageService.getById(id)
                    ?: return@get call.respondText("Message not found", status = HttpStatusCode.NotFound)

                call.respond(HttpStatusCode.OK, message)
            } catch (e: Exception) {
                call.respondText("Error: ${e.message}", status = HttpStatusCode.InternalServerError)
            }
        }

        // DELETE /messages/{id} → delete message
        delete("{id}") {
            try {
                val id = call.parameters["id"]?.toIntOrNull()
                    ?: return@delete call.respondText("Invalid ID", status = HttpStatusCode.BadRequest)

                val deleted = MessageService.delete(id)
                if (deleted) {
                    call.respondText("Message deleted successfully", status = HttpStatusCode.OK)
                } else {
                    call.respondText("Message not found", status = HttpStatusCode.NotFound)
                }
            } catch (e: Exception) {
                call.respondText("Error: ${e.message}", status = HttpStatusCode.InternalServerError)
            }
        }
    }
}
