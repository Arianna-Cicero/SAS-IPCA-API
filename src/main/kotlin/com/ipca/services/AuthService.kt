package com.ipca.services

import com.ipca.auth.PasswordUtils
import com.ipca.models.CollaboratorTable
import com.ipca.models.SessionTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.UUID

data class SessionResponse(
    val sessionToken: UUID,
    val collaboratorId: UUID,
    val expiresAt: LocalDateTime
)

object AuthService {

    fun login(email: String, password: String): SessionResponse {
        return transaction {
            val collaborator = CollaboratorTable
                .select { CollaboratorTable.email eq email }
                .singleOrNull()
                ?: throw Exception("Invalid email or password")

            if (!PasswordUtils.verify(password, collaborator[CollaboratorTable.password])) {
                throw Exception("Invalid email or password")
            }

            val token = UUID.randomUUID()
            val expires = LocalDateTime.now().plus(8, ChronoUnit.HOURS)

            SessionTable.insert {
                it[id] = token
                it[id_collaborator] = collaborator[CollaboratorTable.id_collaborator]
                it[created_at] = LocalDateTime.now()
                it[expires_at] = expires
            }

            SessionResponse(
                sessionToken = token,
                collaboratorId = collaborator[CollaboratorTable.id_collaborator],
                expiresAt = expires
            )
        }
    }

    fun validateToken(token: UUID): UUID? {
        return transaction {
            val session = SessionTable
                .select { SessionTable.id eq token and (SessionTable.active eq true) }
                .singleOrNull()
                ?: return@transaction null

            val expiresAt = session[SessionTable.expires_at]
            if (expiresAt.isBefore(LocalDateTime.now())) {
                return@transaction null
            }

            session[SessionTable.id_collaborator]
        }
    }

    fun validateSession(tokenStr: String): Boolean {
        return try {
            val token = UUID.fromString(tokenStr)
            validateToken(token) != null
        } catch (e: Exception) {
            false
        }
    }

    fun logout(tokenStr: String) {
        try {
            val token = UUID.fromString(tokenStr)
            transaction {
                SessionTable.update({ SessionTable.id eq token }) { row ->
                    row[active] = false
                }
            }
        } catch (e: Exception) {
            // Token format error, ignore
        }
    }
}
