package com.ipca.models

import org.jetbrains.exposed.sql.Table

object MessageTable : Table("message") {
    val id = integer("id_message").autoIncrement()
    val name = varchar("name", 50)
    val email = varchar("email", 50)
    val category = varchar("category", 20)
    val message = text("message")

    override val primaryKey = PrimaryKey(id, name = "PK_Message_ID")
}
