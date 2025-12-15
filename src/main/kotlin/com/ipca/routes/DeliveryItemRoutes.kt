package com.ipca.routes

import com.ipca.services.BeneficiaryService
import com.ipca.services.CourseService
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class DeliveryItemRequest(
    val idDelivery: UUID,
    val idGood: Integer,
    val quantity: Integer
)

fun Route.deliveryItemRoutes() {
    route("/collaborator") {


    }
}