package com.ipca.routes

import com.ipca.dto.Beneficiary.BeneficiaryCreateDTO
import com.ipca.dto.Beneficiary.BeneficiaryUpdateDTO
import com.ipca.services.BeneficiaryService
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.http.*

fun Route.beneficiaryRoutes() {

    route("/beneficiaries") {

        // List all beneficiaries
        get {
            try {
                call.respond(BeneficiaryService.getAll())
            } catch (e: Exception) {
                call.respondText("Error: ${e.message}", status = HttpStatusCode.InternalServerError)
            }
        }

        // Get single beneficiary
        get("{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
                ?: return@get call.respondText("Invalid ID", status = HttpStatusCode.BadRequest)

            val beneficiary = BeneficiaryService.getById(id)
                ?: return@get call.respondText("Beneficiary not found", status = HttpStatusCode.NotFound)

            call.respond(beneficiary)
        }

        // Create beneficiary
        post {
            try {
                val req = call.receive<BeneficiaryCreateDTO>()
                val id = BeneficiaryService.create(req)
                call.respond(HttpStatusCode.Created, mapOf("message" to "Beneficiary created", "id" to id))
            } catch (e: Exception) {
                call.respondText("Error: ${e.message}", status = HttpStatusCode.BadRequest)
            }
        }

        // Update beneficiary
        put("{id}") {
            try {
                val id = call.parameters["id"]?.toIntOrNull()
                    ?: return@put call.respondText("Invalid ID", status = HttpStatusCode.BadRequest)

                val request = call.receive<BeneficiaryUpdateDTO>()
                BeneficiaryService.update(id, request)
                call.respond(mapOf("message" to "Beneficiary updated"))
            } catch (e: Exception) {
                call.respondText("Error: ${e.message}", status = HttpStatusCode.BadRequest)
            }
        }

        // Delete beneficiary
        delete("{id}") {
            try {
                val id = call.parameters["id"]?.toIntOrNull()
                    ?: return@delete call.respondText("Invalid ID", status = HttpStatusCode.BadRequest)

                BeneficiaryService.delete(id)
                call.respond(mapOf("message" to "Beneficiary deleted"))
            } catch (e: Exception) {
                call.respondText("Error: ${e.message}", status = HttpStatusCode.InternalServerError)
            }
        }

        // List schedulings for beneficiary
        get("{id}/scheduling") {
            val id = call.parameters["id"]?.toIntOrNull()
                ?: return@get call.respondText("Invalid ID", status = HttpStatusCode.BadRequest)

            val list = BeneficiaryService.getSchedulingForBeneficiary(id)
            call.respond(list)
        }

        // List deliveries for beneficiary
        get("{id}/deliveries") {
            val id = call.parameters["id"]?.toIntOrNull()
                ?: return@get call.respondText("Invalid ID", status = HttpStatusCode.BadRequest)

            val list = BeneficiaryService.getDeliveriesForBeneficiary(id)
            call.respond(list)
        }
    }
    }
