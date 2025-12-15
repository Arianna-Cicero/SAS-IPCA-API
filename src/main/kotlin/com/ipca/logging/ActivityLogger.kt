package com.ipca.logging

import com.ipca.models.ActivityLogTable
import com.ipca.models.EntityTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

object ActivityLogger {

    fun log(
        collaboratorId: UUID,
        entityType: EntityType,
        entityRecordId: String?,
        action: String
    ) {
        transaction {
            val entityId = EntityTable
                .select { EntityTable.name eq entityType.value }
                .single()[EntityTable.id]

            ActivityLogTable.insert {
                it[ActivityLogTable.collaboratorId] = collaboratorId
                it[ActivityLogTable.entityId] = entityId
                it[recordId] = entityRecordId
                it[ActivityLogTable.action] = action
            }
        }
    }
}
