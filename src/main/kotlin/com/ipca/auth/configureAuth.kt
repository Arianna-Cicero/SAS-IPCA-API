package com.ipca.auth

import com.ipca.services.AuthService
import io.ktor.server.application.*
import io.ktor.server.auth.*
import java.util.UUID

fun Application.configureAuth() {

    authentication {
        bearer("auth-bearer") {
            authenticate { bearerToken ->

                val token = try {
                    UUID.fromString(bearerToken.token)
                } catch (e: Exception) {
                    return@authenticate null
                }

                val collaboratorId = AuthService.validateToken(token)
                if (collaboratorId != null) {
                    CollaboratorPrincipal(collaboratorId)
                } else {
                    null
                }
            }
        }
    }
}
