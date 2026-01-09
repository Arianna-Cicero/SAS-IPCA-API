package com.ipca.routes

import com.ipca.dto.common.CreateResponseDTO
import com.ipca.dto.Collaborator.CollaboratorCreateDTO
import com.ipca.dto.Collaborator.CollaboratorUpdateDTO
import com.ipca.services.CollaboratorService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.server.routing.*

fun Route.collaboratorRoutes() {
    route("/collaborators") {
        // GET /collaborators → list all collaborators
        get {
            try {
                val collaborators = CollaboratorService.getAll()
                call.respond(collaborators)
            } catch (e: Exception) {
                call.respondText("Error: ${e.message}", status = HttpStatusCode.InternalServerError)
            }
        }

        // POST /collaborators → create collaborator
        post {
            try {
                val request = call.receive<CollaboratorCreateDTO>()
                val id = CollaboratorService.create(request)
                call.respond(HttpStatusCode.Created, CreateResponseDTO("Collaborator created", id.toString()))
            } catch (e: Exception) {
                call.respondText("Error: ${e.message}", status = HttpStatusCode.BadRequest)
            }
        }

        // GET /collaborators/{id} → get single collaborator
        get("{id}") {
            try {
                val id = call.parameters["id"]
                    ?: return@get call.respondText("Invalid ID", status = HttpStatusCode.BadRequest)

                val collaborator = CollaboratorService.getById(id)
                    ?: return@get call.respondText("Collaborator not found", status = HttpStatusCode.NotFound)

                call.respond(collaborator)
            } catch (e: Exception) {
                call.respondText("Error: ${e.message}", status = HttpStatusCode.InternalServerError)
            }
        }

        // PUT /collaborators/{id} → update collaborator
        put("{id}") {
            try {
                val id = call.parameters["id"]
                    ?: return@put call.respondText("Invalid ID", status = HttpStatusCode.BadRequest)

                val request = call.receive<CollaboratorUpdateDTO>()
                CollaboratorService.update(id, request)
                call.respond(mapOf("message" to "Collaborator updated"))
            } catch (e: Exception) {
                call.respondText("Error: ${e.message}", status = HttpStatusCode.BadRequest)
            }
        }

        // DELETE /collaborators/{id} → delete collaborator
        delete("{id}") {
            try {
                val id = call.parameters["id"]
                    ?: return@delete call.respondText("Invalid ID", status = HttpStatusCode.BadRequest)

                CollaboratorService.delete(id)
                call.respond(mapOf("message" to "Collaborator deleted"))
            } catch (e: Exception) {
                call.respondText("Error: ${e.message}", status = HttpStatusCode.InternalServerError)
            }
        }
    }
}