package com.ipca.routes

import com.ipca.dto.Good.GoodCreateDTO
import com.ipca.dto.Good.GoodUpdateDTO
import com.ipca.services.GoodService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.server.routing.*

fun Route.goodRoutes() {
    route("/goods") {
        // GET /goods → list all goods
        get {
            try {
                val goods = GoodService.getAll()
                call.respond(goods)
            } catch (e: Exception) {
                call.respondText("Error: ${e.message}", status = HttpStatusCode.InternalServerError)
            }
        }

        // POST /goods → create good
        post {
            try {
                val request = call.receive<GoodCreateDTO>()
                val id = GoodService.create(request)
                call.respond(HttpStatusCode.Created, mapOf("message" to "Good created", "id" to id))
            } catch (e: Exception) {
                call.respondText("Error: ${e.message}", status = HttpStatusCode.BadRequest)
            }
        }

        // GET /goods/{id} → get single good
        get("{id}") {
            try {
                val id = call.parameters["id"]?.toIntOrNull()
                    ?: return@get call.respondText("Invalid ID", status = HttpStatusCode.BadRequest)

                val good = GoodService.getById(id)
                    ?: return@get call.respondText("Good not found", status = HttpStatusCode.NotFound)

                call.respond(good)
            } catch (e: Exception) {
                call.respondText("Error: ${e.message}", status = HttpStatusCode.InternalServerError)
            }
        }

        // PUT /goods/{id} → update good
        put("{id}") {
            try {
                val id = call.parameters["id"]?.toIntOrNull()
                    ?: return@put call.respondText("Invalid ID", status = HttpStatusCode.BadRequest)

                val request = call.receive<GoodUpdateDTO>()
                GoodService.update(id, request)
                call.respond(mapOf("message" to "Good updated"))
            } catch (e: Exception) {
                call.respondText("Error: ${e.message}", status = HttpStatusCode.BadRequest)
            }
        }

        // DELETE /goods/{id} → delete good
        delete("{id}") {
            try {
                val id = call.parameters["id"]?.toIntOrNull()
                    ?: return@delete call.respondText("Invalid ID", status = HttpStatusCode.BadRequest)

                GoodService.delete(id)
                call.respond(mapOf("message" to "Good deleted"))
            } catch (e: Exception) {
                call.respondText("Error: ${e.message}", status = HttpStatusCode.InternalServerError)
            }
        }

        // GET /goods/category/{category} → list goods by category
        get("category/{category}") {
            try {
                val category = call.parameters["category"]
                    ?: return@get call.respondText("Invalid category", status = HttpStatusCode.BadRequest)

                val goods = GoodService.getByCategory(category)
                call.respond(goods)
            } catch (e: Exception) {
                call.respondText("Error: ${e.message}", status = HttpStatusCode.InternalServerError)
            }
        }
    }
}
