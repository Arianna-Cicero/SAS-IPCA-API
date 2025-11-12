package com.ipca.routes

import com.ipca.services.BeneficiaryService
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

@Serializable
data class BeneficiaryRequest(
        val name: String,
        val studentNumber: Int,
        val email: String ,
        val idCourse: Int,
        val telephone: String
)

fun Route.beneficiaryRoutes() {
    route("/beneficiaries") {

        // GET /beneficiaries → listar todos os beneficiários
        get {
            val beneficiaries = BeneficiaryService.getAll()
            call.respond(beneficiaries)
        }

        // POST /beneficiaries → adicionar um novo beneficiário
        post {
            val request = call.receive<BeneficiaryRequest>()
            BeneficiaryService.add(
                    name = request.name,
                    studentNumber = request.studentNumber,
                    email = request.email,
                    idCourse = request.idCourse,
                    telephone = request.telephone
            )
            call.respond(mapOf("message" to "Beneficiary added successfully"))
        }
    }
}
