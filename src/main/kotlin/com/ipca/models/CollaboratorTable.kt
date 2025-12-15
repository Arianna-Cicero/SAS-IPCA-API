package com.ipca.models

import org.jetbrains.exposed.sql.Table

object CollaboratorTable : Table("Collaborator"){
    val id_collaborator = uuid("id_collaborator").autoGenerate()
    val name = varchar("name", 100)
    val email = varchar("email", 150).uniqueIndex()
    val password = varchar("password_hash", 255)
    val profile = varchar("profile", 20)
    val active = bool("active").default(true)

    override val primaryKey = PrimaryKey(id_collaborator, name = "PK_Collaborator_ID")
}