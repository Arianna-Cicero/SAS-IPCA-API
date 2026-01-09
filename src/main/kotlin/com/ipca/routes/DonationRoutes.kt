package com.ipca.routes

import com.ipca.dto.common.CreateResponseDTO
import com.ipca.dto.Donation.DonationCreateDTO
import com.ipca.dto.Donation.DonationUpdateDTO
import com.ipca.services.DonationService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.server.routing.*

fun Route.donationRoutes() {
    route("/donations") {
        // GET /donations → list all donations
        get {
            try {
                val donations = DonationService.getAll()
                call.respond(donations)
            } catch (e: Exception) {
                call.respondText("Error: ${e.message}", status = HttpStatusCode.InternalServerError)
            }
        }

        // POST /donations → create donation
        post {
            try {
                val request = call.receive<DonationCreateDTO>()
                val id = DonationService.create(request)
                call.respond(HttpStatusCode.Created, CreateResponseDTO("Donation created", id.toString()))
            } catch (e: Exception) {
                call.respondText("Error: ${e.message}", status = HttpStatusCode.BadRequest)
            }
        }

        // GET /donations/{id} → get single donation
        get("{id}") {
            try {
                val id = call.parameters["id"]
                    ?: return@get call.respondText("Invalid ID", status = HttpStatusCode.BadRequest)

                val donation = DonationService.getById(id)
                    ?: return@get call.respondText("Donation not found", status = HttpStatusCode.NotFound)

                call.respond(donation)
            } catch (e: Exception) {
                call.respondText("Error: ${e.message}", status = HttpStatusCode.InternalServerError)
            }
        }

        // PUT /donations/{id} → update donation
        put("{id}") {
            try {
                val id = call.parameters["id"]
                    ?: return@put call.respondText("Invalid ID", status = HttpStatusCode.BadRequest)

                val request = call.receive<DonationUpdateDTO>()
                DonationService.update(id, request)
                call.respond(mapOf("message" to "Donation updated"))
            } catch (e: Exception) {
                call.respondText("Error: ${e.message}", status = HttpStatusCode.BadRequest)
            }
        }

        // DELETE /donations/{id} → delete donation
        delete("{id}") {
            try {
                val id = call.parameters["id"]
                    ?: return@delete call.respondText("Invalid ID", status = HttpStatusCode.BadRequest)

                DonationService.delete(id)
                call.respond(mapOf("message" to "Donation deleted"))
            } catch (e: Exception) {
                call.respondText("Error: ${e.message}", status = HttpStatusCode.InternalServerError)
            }
        }
    }
}
