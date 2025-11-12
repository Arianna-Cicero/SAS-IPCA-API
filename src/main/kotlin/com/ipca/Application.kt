package com.ipca

import com.ipca.database.DatabaseFactory
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import com.ipca.routes.courseRoutes
import com.ipca.routes.beneficiaryRoutes

fun main() {
    embeddedServer(Netty, port = 8080, module = Application::module).start(wait = true)
}

fun Application.module() {
    install(ContentNegotiation) {
        json()
    }

    DatabaseFactory.init()

    routing {
        get("/") {
            call.respondText("Loja Social IPCA API is running")
        }

        courseRoutes()
        beneficiaryRoutes()
    }
}
