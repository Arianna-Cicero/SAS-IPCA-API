package com.ipca.services

import com.ipca.auth.PasswordUtils
import com.ipca.dto.Collaborator.CollaboratorCreateDTO
import com.ipca.dto.Collaborator.CollaboratorResponseDTO
import com.ipca.dto.Collaborator.CollaboratorUpdateDTO
import com.ipca.models.CollaboratorTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

object CollaboratorService {

    fun getAll(): List<CollaboratorResponseDTO> = transaction {
        CollaboratorTable.selectAll().map { row ->
            CollaboratorResponseDTO(
                id = row[CollaboratorTable.id_collaborator],
                name = row[CollaboratorTable.name],
                email = row[CollaboratorTable.email],
                profile = row[CollaboratorTable.profile],
                active = row[CollaboratorTable.active]
            )
        }
    }

    fun getById(id: String): CollaboratorResponseDTO? = transaction {
        try {
            val uuid = UUID.fromString(id)
            CollaboratorTable
                .select { CollaboratorTable.id_collaborator eq uuid }
                .singleOrNull()
                ?.let { row ->
                    CollaboratorResponseDTO(
                        id = row[CollaboratorTable.id_collaborator],
                        name = row[CollaboratorTable.name],
                        email = row[CollaboratorTable.email],
                        profile = row[CollaboratorTable.profile],
                        active = row[CollaboratorTable.active]
                    )
                }
        } catch (e: Exception) {
            null
        }
    }

    fun create(request: CollaboratorCreateDTO): String = transaction {
        val id = UUID.randomUUID()
        val hashedPassword = PasswordUtils.hash(request.password)

        CollaboratorTable.insert {
            it[id_collaborator] = id
            it[name] = request.name
            it[email] = request.email
            it[password] = hashedPassword
            it[profile] = request.profile
            it[active] = true
        }

        id.toString()
    }

    fun update(id: String, request: CollaboratorUpdateDTO) = transaction {
        try {
            val uuid = UUID.fromString(id)
            CollaboratorTable.update({ CollaboratorTable.id_collaborator eq uuid }) { row ->
                request.name?.let { row[CollaboratorTable.name] = it }
                request.email?.let { row[CollaboratorTable.email] = it }
                request.profile?.let { row[CollaboratorTable.profile] = it }
                request.active?.let { row[CollaboratorTable.active] = it }
            }
        } catch (e: Exception) {
            // Invalid UUID format
        }
    }

    fun delete(id: String) = transaction {
        try {
            val uuid = UUID.fromString(id)
            CollaboratorTable.deleteWhere { CollaboratorTable.id_collaborator eq uuid }
        } catch (e: Exception) {
            // Invalid UUID format
        }
    }
}