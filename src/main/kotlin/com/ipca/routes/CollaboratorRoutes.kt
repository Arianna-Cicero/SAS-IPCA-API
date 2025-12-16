package com.ipca.routes

import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

@Serializable
data class CollaboratorRequest(val name: String)

fun Route.collaboratorRoutes() {
    route("/collaborator") {


    }
}