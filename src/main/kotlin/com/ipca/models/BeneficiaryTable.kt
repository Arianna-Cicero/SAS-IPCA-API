package com.ipca.models

import org.jetbrains.exposed.dao.id.IntIdTable
import java.util.UUID
import org.jetbrains.exposed.sql.Table

object BeneficiaryTable : IntIdTable("Beneficiary") {
    val name = varchar("name", 100)
    val studentNumber = integer("student_number")
    val email = varchar("email", 100)
    val idCourse = integer("id_course").references(CourseTable.id, onDelete = org.jetbrains.exposed.sql.ReferenceOption.SET_NULL)
    val telephone = varchar("telephone", 50)
}
