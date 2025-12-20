package com.ipca.services

import com.ipca.dto.ActivityLog.ActivityLogCreateDTO
import com.ipca.dto.ActivityLog.ActivityLogResponseDTO
import com.ipca.models.ActivityLogTable
import com.ipca.models.EntityTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

object ActivityLogService {

    fun getAll(): List<ActivityLogResponseDTO> = transaction {
        ActivityLogTable.selectAll().map { row ->
            val entityId = row[ActivityLogTable.entityId]
            val entity = EntityTable
                .select { EntityTable.id eq entityId }
                .singleOrNull()

            ActivityLogResponseDTO(
                id = row[ActivityLogTable.id],
                collaboratorId = row[ActivityLogTable.collaboratorId],
                entityId = entityId,
                entityName = entity?.get(EntityTable.name) ?: "",
                recordId = row[ActivityLogTable.recordId],
                action = row[ActivityLogTable.action],
                timestamp = row[ActivityLogTable.timestamp]
            )
        }
    }

    fun getById(id: Int): ActivityLogResponseDTO? = transaction {
        ActivityLogTable
            .select { ActivityLogTable.id eq id }
            .singleOrNull()
            ?.let { row ->
                val entityId = row[ActivityLogTable.entityId]
                val entity = EntityTable
                    .select { EntityTable.id eq entityId }
                    .singleOrNull()

                ActivityLogResponseDTO(
                    id = row[ActivityLogTable.id],
                    collaboratorId = row[ActivityLogTable.collaboratorId],
                    entityId = entityId,
                    entityName = entity?.get(EntityTable.name) ?: "",
                    recordId = row[ActivityLogTable.recordId],
                    action = row[ActivityLogTable.action],
                    timestamp = row[ActivityLogTable.timestamp]
                )
            }
    }

    fun create(request: ActivityLogCreateDTO): Int = transaction {
        ActivityLogTable.insertAndGetId {
            it[collaboratorId] = request.collaboratorId
            it[entityId] = request.entityId
            it[recordId] = request.recordId
            it[action] = request.action
        }.value
    }

    fun getByCollaboratorId(collaboratorId: String): List<ActivityLogResponseDTO> = transaction {
        try {
            val uuid = UUID.fromString(collaboratorId)
            ActivityLogTable
                .select { ActivityLogTable.collaboratorId eq uuid }
                .map { row ->
                    val entityId = row[ActivityLogTable.entityId]
                    val entity = EntityTable
                        .select { EntityTable.id eq entityId }
                        .singleOrNull()

                    ActivityLogResponseDTO(
                        id = row[ActivityLogTable.id],
                        collaboratorId = row[ActivityLogTable.collaboratorId],
                        entityId = entityId,
                        entityName = entity?.get(EntityTable.name) ?: "",
                        recordId = row[ActivityLogTable.recordId],
                        action = row[ActivityLogTable.action],
                        timestamp = row[ActivityLogTable.timestamp]
                    )
                }
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun getByEntityId(entityId: Int): List<ActivityLogResponseDTO> = transaction {
        ActivityLogTable
            .select { ActivityLogTable.entityId eq entityId }
            .map { row ->
                val entity = EntityTable
                    .select { EntityTable.id eq entityId }
                    .singleOrNull()

                ActivityLogResponseDTO(
                    id = row[ActivityLogTable.id],
                    collaboratorId = row[ActivityLogTable.collaboratorId],
                    entityId = entityId,
                    entityName = entity?.get(EntityTable.name) ?: "",
                    recordId = row[ActivityLogTable.recordId],
                    action = row[ActivityLogTable.action],
                    timestamp = row[ActivityLogTable.timestamp]
                )
            }
    }
}
