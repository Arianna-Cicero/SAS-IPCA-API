package com.ipca.models

import org.jetbrains.exposed.dao.id.IntIdTable
import java.util.UUID
import org.jetbrains.exposed.sql.Table

object BeneficiaryTable : Table("beneficiary") {
    val studentNumber = integer("student_number")
    val name = varchar("name", 100)
    val email = varchar("email", 100)
    val idCourse = integer("id_course").references(CourseTable.id, onDelete = org.jetbrains.exposed.sql.ReferenceOption.SET_NULL)
    val telephone = varchar("telephone", 50)
    
    override val primaryKey = PrimaryKey(studentNumber)
}
