package com.ipca

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun main() {
    embeddedServer(Netty, port = 8080, module = Application::module).start(wait = true)
}

fun Application.module() {
    install(ContentNegotiation) {
        json()
    }

    routing {
        get("/") {
            call.respondText("Loja Social IPCA API is running")
        }
<<<<<<< Updated upstream
=======

        authRoutes()

        // Rota pública para criar mensagens (POST sem autenticação)
        post("/messages") {
            try {
                val request = call.receive<MessageCreateDTO>()
                val id = MessageService.addMessage(
                    name = request.name,
                    email = request.email,
                    category = request.category,
                    message = request.message
                )
                call.respond(HttpStatusCode.Created, CreateResponseDTO("Message created successfully", id.toString()))
            } catch (e: Exception) {
                call.respondText("Error: ${e.message}", status = HttpStatusCode.BadRequest)
            }
        }

        // Rotas de goods públicas (sem autenticação)
        goodRoutes()

        authenticate("auth-bearer") {
            dashboardRoutes()
            courseRoutes()
            beneficiaryRoutes()
            collaboratorRoutes()
            deliveryRoutes()
            deliveryItemRoutes()
            donationRoutes()
            expirationAlertRoutes()
            schedulingRoutes()
            newsRoutes()
            activityLogRoutes()
            entityRoutes()
            messageRoutes() // GET, GET/{id}, DELETE protegidos
        }
>>>>>>> Stashed changes
    }
}
