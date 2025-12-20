package com.ipca

import com.ipca.auth.configureAuth
import com.ipca.database.DatabaseFactory
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
import com.ipca.routes.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.insert
import io.ktor.server.auth.*
import io.ktor.server.plugins.openapi.*
import io.ktor.server.plugins.swagger.*




fun main() {
    embeddedServer(Netty, port = 8080, module = Application::module).start(wait = true)
}

fun Application.module() {
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
        openAPI(path = "openapi")
        swaggerUI(path = "swagger", swaggerFile = "openapi/documentation.yaml")

        get("/") {
            call.respondText("Loja Social IPCA API is running")
        }

        authRoutes()

        authenticate("auth-bearer") {
            courseRoutes()
            beneficiaryRoutes()
            collaboratorRoutes()
            deliveryRoutes()
            deliveryItemRoutes()
            donationRoutes()
            goodRoutes()
            expirationAlertRoutes()
            schedulingRoutes()
            newsRoutes()
            activityLogRoutes()
            entityRoutes()
        }
    }
}
