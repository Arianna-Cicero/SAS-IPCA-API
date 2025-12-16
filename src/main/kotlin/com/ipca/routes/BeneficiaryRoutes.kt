package com.ipca.routes

import com.ipca.dto.Beneficiary.BeneficiaryRequest
import com.ipca.services.BeneficiaryService
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.server.auth.*

fun Route.beneficiaryRoutes() {

    route("/beneficiaries") {

        // List all beneficiaries
        get {
            call.respond(BeneficiaryService.getAll())
        }

        // Get single beneficiary
        get("{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
                ?: return@get call.respondText("Invalid ID")

            val beneficiary = BeneficiaryService.getById(id)
                ?: return@get call.respondText("Beneficiary not found")

            call.respond(beneficiary)
        }

        // Create beneficiary
        post {
            val req = call.receive<BeneficiaryRequest>()
            val id = BeneficiaryService.create(req)
            call.respond(mapOf("message" to "Beneficiary created", "id" to id))
        }


        // List schedulings for beneficiary
        get("{id}/scheduling") {
            val id = call.parameters["id"]?.toIntOrNull()
                ?: return@get call.respondText("Invalid ID")

            val list = BeneficiaryService.getSchedulingForBeneficiary(id)
            call.respond(list)
        }

        // List deliveries for beneficiary
        get("{id}/deliveries") {
            val id = call.parameters["id"]?.toIntOrNull()
                ?: return@get call.respondText("Invalid ID")

            val list = BeneficiaryService.getDeliveriesForBeneficiary(id)
            call.respond(list)
        }
    }
}
