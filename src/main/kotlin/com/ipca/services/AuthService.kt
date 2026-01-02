package com.ipca.services

import com.ipca.auth.JwtProvider
import com.ipca.auth.PasswordUtils
import com.ipca.exceptions.AuthenticationException
import com.ipca.models.CollaboratorTable
import com.ipca.models.SessionTable
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.UUID

@Serializable
data class LoginRequest(
    val email: String,
    val password: String
)

@Serializable
data class LoginResponse(
    val token: String,
    val collaboratorId: String,
    val email: String,
    val expiresIn: Long = 8 * 3600 // 8 hours in seconds
)

object AuthService {

    fun login(email: String, password: String): LoginResponse {
        return transaction {
            val collaborator = CollaboratorTable
                .select { CollaboratorTable.email eq email }
                .singleOrNull()
                ?: throw AuthenticationException("Invalid email or password")

            if (!PasswordUtils.verify(password, collaborator[CollaboratorTable.password])) {
                throw AuthenticationException("Invalid email or password")
            }

            val collaboratorId = collaborator[CollaboratorTable.id_collaborator]
            val token = JwtProvider.generateToken(collaboratorId, email)

            // Store session in database for audit/tracking purposes
            val sessionId = UUID.randomUUID()
            SessionTable.insert {
                it[id] = sessionId
                it[id_collaborator] = collaboratorId
                it[created_at] = LocalDateTime.now()
                it[expires_at] = LocalDateTime.now().plus(8, ChronoUnit.HOURS)
            }

            LoginResponse(
                token = token,
                collaboratorId = collaboratorId.toString(),
                email = email,
                expiresIn = 8 * 3600
            )
        }
    }

    fun validateToken(token: String): UUID? {
        return try {
            val tokenPayload = JwtProvider.verifyToken(token)
            tokenPayload.collaboratorId
        } catch (e: Exception) {
            null
        }
    }

    fun validateSession(tokenStr: String): Boolean {
        return JwtProvider.isTokenValid(tokenStr)
    }

    fun logout(tokenStr: String) {
        // In a real app, you might want to blacklist the token
        // For now, just validate it exists
        try {
            JwtProvider.verifyToken(tokenStr)
        } catch (e: Exception) {
            throw AuthenticationException("Invalid token for logout")
        }
    }
}
