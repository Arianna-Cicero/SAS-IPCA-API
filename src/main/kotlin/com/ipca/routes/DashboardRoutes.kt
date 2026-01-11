package com.ipca.routes

import com.ipca.dto.Dashboard.DashboardOverviewDTO
import com.ipca.services.DashboardService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.dashboardRoutes() {
    
    get("/dashboard/overview") {
        try {
            val overview = DashboardService.getOverview()
            call.respond(HttpStatusCode.OK, overview)
        } catch (e: Exception) {
            call.respondText("Error: ${e.message}", status = HttpStatusCode.InternalServerError)
        }
    }
}
