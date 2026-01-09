package com.ipca

import com.ipca.auth.configureAuth
import com.ipca.database.DatabaseFactory
import com.ipca.exceptions.configureErrorHandling
import com.ipca.logging.EntityType
import com.ipca.models.EntityTable
import com.ipca.routes.authRoutes
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.request.*
import com.ipca.routes.*
import com.ipca.dto.Message.MessageCreateDTO
import com.ipca.dto.common.CreateResponseDTO
import com.ipca.services.MessageService
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.insert
import io.ktor.server.auth.*
import io.ktor.server.plugins.openapi.*
import io.ktor.server.plugins.swagger.*
import io.ktor.http.*
import io.ktor.server.plugins.cors.routing.*




fun main() {
    embeddedServer(Netty, port = 8080, module = Application::module).start(wait = true)
}

fun Application.module() {
    // Install error handling first
    configureErrorHandling()
    
    // Configure CORS for web/mobile apps
    install(CORS) {
        // Modo desenvolvimento - permite qualquer origem
        anyHost()
        
        // Headers permitidos
        allowHeader(HttpHeaders.ContentType)
        allowHeader(HttpHeaders.Authorization)
        allowHeader(HttpHeaders.AccessControlAllowOrigin)
        allowHeader(HttpHeaders.AccessControlAllowHeaders)
        allowHeader("X-Requested-With")
        allowHeader(HttpHeaders.Accept)
        
        // Métodos HTTP permitidos
        allowMethod(HttpMethod.Get)
        allowMethod(HttpMethod.Post)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Patch)
        
        // Permitir credenciais (cookies, authorization headers)
        allowCredentials = true
        
        // Cache da resposta preflight por 1 hora
        maxAgeInSeconds = 3600
    }
    
    install(ContentNegotiation) {
        json()
    }

    DatabaseFactory.init()
    configureAuth()

    transaction {
        val defaults = EntityType.values().map { it.value }

        defaults.forEach { name ->
            if (EntityTable.select { EntityTable.name eq name }.empty()) {
                EntityTable.insert { it[EntityTable.name] = name }
            }
        }
    }

    routing {
        // Serve the static OpenAPI YAML from resources without http-content plugin
        get("/openapi/documentation.yaml") {
            val resource = this::class.java.classLoader.getResource("openapi/documentation.yaml")
            val yaml = resource?.readText() ?: return@get call.respondText("Spec not found", status = HttpStatusCode.NotFound)
            call.respondText(yaml, ContentType.parse("application/yaml"))
        }
        swaggerUI(path = "swagger", swaggerFile = "openapi/documentation.yaml")

        get("/") {
            call.respondText("Loja Social IPCA API is running")
        }

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
    }
}
