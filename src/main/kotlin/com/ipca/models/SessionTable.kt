package com.ipca.models

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime

object SessionTable : Table("Session"){
    val id = uuid("id_session").autoGenerate()
    val id_collaborator = uuid("id_collaborator")
    val created_at = datetime("created_at")
    val expires_at = datetime("expires_at")
    val active = bool("active").default(true)

    override val primaryKey = PrimaryKey(SessionTable.id, name = "PK_Session_ID")
}