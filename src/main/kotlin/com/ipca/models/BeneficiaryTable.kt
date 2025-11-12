package com.ipca.models

import java.util.UUID
import org.jetbrains.exposed.sql.Table

object BeneficiaryTable : Table(){
    val id = integer("id_beneficiary").autoIncrement()
    val name = varchar("name", 100)
    val student_number = integer("student_number")
    val email = varchar("email",100)
    val id_course = integer("id_course").references(CourseTable.id)
    val telephone = varchar("telephone", 50)

    override val primaryKey = PrimaryKey(id, name = "PK_Beneficiary_ID")
}