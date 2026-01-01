package com.ipca.routes

import com.ipca.dto.Entity.EntityCreateDTO
import com.ipca.services.EntityService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.server.routing.*

fun Route.entityRoutes() {
    route("/entities") {
        // GET /entities → list all entities
        get {
            try {
                val entities = EntityService.getAll()
                call.respond(entities)
            } catch (e: Exception) {
                call.respondText("Error: ${e.message}", status = HttpStatusCode.InternalServerError)
            }
        }

        // POST /entities → create entity
        post {
            try {
                val request = call.receive<EntityCreateDTO>()
                val id = EntityService.create(request)
                call.respond(HttpStatusCode.Created, mapOf("message" to "Entity created", "id" to id))
            } catch (e: Exception) {
                call.respondText("Error: ${e.message}", status = HttpStatusCode.BadRequest)
            }
        }

        // GET /entities/{id} → get single entity
        get("{id}") {
            try {
                val id = call.parameters["id"]?.toIntOrNull()
                    ?: return@get call.respondText("Invalid ID", status = HttpStatusCode.BadRequest)

                val entity = EntityService.getById(id)
                    ?: return@get call.respondText("Entity not found", status = HttpStatusCode.NotFound)

                call.respond(entity)
            } catch (e: Exception) {
                call.respondText("Error: ${e.message}", status = HttpStatusCode.InternalServerError)
            }
        }

        // GET /entities/by-name/{name} → get entity by name
        get("by-name/{name}") {
            try {
                val name = call.parameters["name"]
                    ?: return@get call.respondText("Invalid name", status = HttpStatusCode.BadRequest)

                val entity = EntityService.getByName(name)
                    ?: return@get call.respondText("Entity not found", status = HttpStatusCode.NotFound)

                call.respond(entity)
            } catch (e: Exception) {
                call.respondText("Error: ${e.message}", status = HttpStatusCode.InternalServerError)
            }
        }
    }
}
