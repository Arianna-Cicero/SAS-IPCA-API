package com.ipca.routes

import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import java.util.UUID
import java.util.Date

@Serializable
data class DeliveryRequest(
    val id: UUID,
    val idScheduling: Integer,
    val dateDelivery: Date,
    val status: String,
)

fun Route.deliveryRoutes() {
    route("/collaborator") {


    }
}