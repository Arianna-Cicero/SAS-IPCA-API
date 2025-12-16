package com.ipca.models

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime

object ActivityLogTable : Table("Activity_Log") {
    val id = integer("id_log").autoIncrement()

    val collaboratorId = uuid("id_collaborator")
        .references(CollaboratorTable.id_collaborator, onDelete = ReferenceOption.NO_ACTION)

    val entityId = integer("id_entity")
        .references(EntityTable.id, onDelete = ReferenceOption.NO_ACTION)

    val recordId = varchar("entity_id", 100).nullable()

    val action = varchar("action", 150)
    val timestamp = datetime("timestamp").defaultExpression(CurrentDateTime)

    override val primaryKey = PrimaryKey(id)
}
