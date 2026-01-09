package com.ipca.routes

import com.ipca.dto.common.CreateResponseDTO
import com.ipca.dto.Schedulling.SchedulingCreateDTO
import com.ipca.dto.Schedulling.SchedulingUpdateDTO
import com.ipca.services.SchedulingService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.schedulingRoutes() {
    route("/scheduling") {
        // GET /scheduling → list all schedulings
        get {
            try {
                val schedulings = SchedulingService.getAll()
                call.respond(schedulings)
            } catch (e: Exception) {
                call.respondText("Error: ${e.message}", status = HttpStatusCode.InternalServerError)
            }
        }

        // POST /scheduling → create scheduling
        post {
            try {
                val request = call.receive<SchedulingCreateDTO>()
                val id = SchedulingService.create(request)
                call.respond(HttpStatusCode.Created, CreateResponseDTO("Scheduling created", id.toString()))
            } catch (e: Exception) {
                call.respondText("Error: ${e.message}", status = HttpStatusCode.BadRequest)
            }
        }

        // GET /scheduling/{id} → get single scheduling
        get("{id}") {
            try {
                val id = call.parameters["id"]?.toIntOrNull()
                    ?: return@get call.respondText("Invalid ID", status = HttpStatusCode.BadRequest)

                val scheduling = SchedulingService.getById(id)
                    ?: return@get call.respondText("Scheduling not found", status = HttpStatusCode.NotFound)

                call.respond(scheduling)
            } catch (e: Exception) {
                call.respondText("Error: ${e.message}", status = HttpStatusCode.InternalServerError)
            }
        }

        // PUT /scheduling/{id} → update scheduling
        put("{id}") {
            try {
                val id = call.parameters["id"]?.toIntOrNull()
                    ?: return@put call.respondText("Invalid ID", status = HttpStatusCode.BadRequest)

                val request = call.receive<SchedulingUpdateDTO>()
                SchedulingService.update(id, request)
                call.respond(mapOf("message" to "Scheduling updated"))
            } catch (e: Exception) {
                call.respondText("Error: ${e.message}", status = HttpStatusCode.BadRequest)
            }
        }

        // DELETE /scheduling/{id} → delete scheduling
        delete("{id}") {
            try {
                val id = call.parameters["id"]?.toIntOrNull()
                    ?: return@delete call.respondText("Invalid ID", status = HttpStatusCode.BadRequest)

                SchedulingService.delete(id)
                call.respond(mapOf("message" to "Scheduling deleted"))
            } catch (e: Exception) {
                call.respondText("Error: ${e.message}", status = HttpStatusCode.InternalServerError)
            }
        }
    }
}
