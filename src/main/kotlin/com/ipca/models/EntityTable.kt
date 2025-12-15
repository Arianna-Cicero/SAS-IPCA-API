package com.ipca.models

import org.jetbrains.exposed.sql.Table

object EntityTable : Table("Entity") {
    val id = integer("id_entity").autoIncrement()
    val name = varchar("name", 100).uniqueIndex()

    override val primaryKey = PrimaryKey(id)
}
