package com.ipca.models

import java.util.UUID
import org.jetbrains.exposed.sql.Table

object CollaboratorTable : Table("Collaborator"){
    val id = uuid("id_collaborator").autoGenerate()
    val name = varchar("name", 100)
    val email = varchar("email", 150).uniqueIndex()
    val password = varchar("password_hash", 255)
    val profile = varchar("profile", 20)
    val active = bool("active").default(true)

    override val primaryKey = PrimaryKey(id, name = "PK_Collaborator_ID")
}