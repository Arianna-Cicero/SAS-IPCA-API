package com.ipca.services

import com.ipca.dto.ActivityLog.ActivityLogCreateDTO
import com.ipca.dto.ActivityLog.ActivityLogResponseDTO
import com.ipca.models.ActivityLogTable
import com.ipca.models.EntityTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
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
                collaboratorId = row[ActivityLogTable.collaboratorId].toString(),
                entityId = entityId,
                entityName = entity?.get(EntityTable.name) ?: "",
                recordId = row[ActivityLogTable.recordId],
                action = row[ActivityLogTable.action],
                timestamp = row[ActivityLogTable.timestamp].toString()
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
                    collaboratorId = row[ActivityLogTable.collaboratorId].toString(),
                    entityId = entityId,
                    entityName = entity?.get(EntityTable.name) ?: "",
                    recordId = row[ActivityLogTable.recordId],
                    action = row[ActivityLogTable.action],
                    timestamp = row[ActivityLogTable.timestamp].toString()
                )
            }
    }

    fun create(request: ActivityLogCreateDTO): Int = transaction {
        val insert = ActivityLogTable.insert { row ->
            row[collaboratorId] = UUID.fromString(request.collaboratorId)
            row[entityId] = request.entityId
            row[recordId] = request.recordId
            row[action] = request.action
        }
        insert.resultedValues?.first()?.get(ActivityLogTable.id)
            ?: error("Failed to insert ActivityLog")
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
                        collaboratorId = row[ActivityLogTable.collaboratorId].toString(),
                        entityId = entityId,
                        entityName = entity?.get(EntityTable.name) ?: "",
                        recordId = row[ActivityLogTable.recordId],
                        action = row[ActivityLogTable.action],
                        timestamp = row[ActivityLogTable.timestamp].toString()
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
                    collaboratorId = row[ActivityLogTable.collaboratorId].toString(),
                    entityId = entityId,
                    entityName = entity?.get(EntityTable.name) ?: "",
                    recordId = row[ActivityLogTable.recordId],
                    action = row[ActivityLogTable.action],
                    timestamp = row[ActivityLogTable.timestamp].toString()
                )
            }
    }
}
