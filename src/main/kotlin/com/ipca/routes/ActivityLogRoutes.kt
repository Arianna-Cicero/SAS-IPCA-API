package com.ipca.routes

import com.ipca.dto.ActivityLog.ActivityLogCreateDTO
import com.ipca.dto.common.CreateResponseDTO
import com.ipca.services.ActivityLogService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.server.routing.*

fun Route.activityLogRoutes() {
    route("/activity-logs") {
        // GET /activity-logs → list all activity logs
        get {
            try {
                val logs = ActivityLogService.getAll()
                call.respond(logs)
            } catch (e: Exception) {
                call.respondText("Error: ${e.message}", status = HttpStatusCode.InternalServerError)
            }
        }

        // POST /activity-logs → create activity log
        post {
            try {
                val request = call.receive<ActivityLogCreateDTO>()
                val id = ActivityLogService.create(request)
                call.respond(HttpStatusCode.Created, CreateResponseDTO("Activity log created", id.toString()))
            } catch (e: Exception) {
                call.respondText("Error: ${e.message}", status = HttpStatusCode.BadRequest)
            }
        }

        // GET /activity-logs/{id} → get single activity log
        get("{id}") {
            try {
                val id = call.parameters["id"]?.toIntOrNull()
                    ?: return@get call.respondText("Invalid ID", status = HttpStatusCode.BadRequest)

                val log = ActivityLogService.getById(id)
                    ?: return@get call.respondText("Activity log not found", status = HttpStatusCode.NotFound)

                call.respond(log)
            } catch (e: Exception) {
                call.respondText("Error: ${e.message}", status = HttpStatusCode.InternalServerError)
            }
        }

        // GET /activity-logs/collaborator/{collaboratorId} → get logs for collaborator
        get("collaborator/{collaboratorId}") {
            try {
                val collaboratorId = call.parameters["collaboratorId"]
                    ?: return@get call.respondText("Invalid collaborator ID", status = HttpStatusCode.BadRequest)

                val logs = ActivityLogService.getByCollaboratorId(collaboratorId)
                call.respond(logs)
            } catch (e: Exception) {
                call.respondText("Error: ${e.message}", status = HttpStatusCode.InternalServerError)
            }
        }

        // GET /activity-logs/entity/{entityId} → get logs for entity
        get("entity/{entityId}") {
            try {
                val entityId = call.parameters["entityId"]?.toIntOrNull()
                    ?: return@get call.respondText("Invalid entity ID", status = HttpStatusCode.BadRequest)

                val logs = ActivityLogService.getByEntityId(entityId)
                call.respond(logs)
            } catch (e: Exception) {
                call.respondText("Error: ${e.message}", status = HttpStatusCode.InternalServerError)
            }
        }

        // DELETE /activity-logs/{id} → delete activity log
        delete("{id}") {
            try {
                val id = call.parameters["id"]?.toIntOrNull()
                    ?: return@delete call.respondText("Invalid ID", status = HttpStatusCode.BadRequest)

                ActivityLogService.delete(id)
                call.respond(HttpStatusCode.OK, mapOf("message" to "Activity log deleted successfully"))
            } catch (e: Exception) {
                call.respondText("Error: ${e.message}", status = HttpStatusCode.InternalServerError)
            }
        }
    }
}
