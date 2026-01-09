package com.ipca.auth

import com.ipca.exceptions.AuthenticationException
import com.ipca.services.AuthService
import io.ktor.server.application.*
import io.ktor.server.auth.*

fun Application.configureAuth() {
    JwtProvider.init()

    authentication {
        bearer("auth-bearer") {
            authenticate { bearerToken ->
                try {
                    val tokenPayload = JwtProvider.verifyToken(bearerToken.token)
                    CollaboratorPrincipal(tokenPayload.collaboratorId.toString())
                } catch (e: AuthenticationException) {
                    null
                } catch (e: Exception) {
                    null
                }
            }
        }
    }
}
