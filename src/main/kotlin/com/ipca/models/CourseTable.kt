package com.ipca.models

import org.jetbrains.exposed.sql.Table


object CourseTable : Table("Course") {
    val id = integer("id_course").autoIncrement()
    val name = varchar("name", 100)

    override val primaryKey = PrimaryKey(id, name = "PK_Course_ID")
}
