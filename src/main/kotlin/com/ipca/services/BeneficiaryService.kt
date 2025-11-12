package com.ipca.services

import com.ipca.models.BeneficiaryTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

data class BeneficiaryDTO(
        val id: Int,
        val name: String,
        val studentNumber: Int?,
        val email: String?,
        val idCourse: Int?,
        val telephone: String?
)

object BeneficiaryService {

    fun getAll(): List<BeneficiaryDTO> = transaction {
        BeneficiaryTable.selectAll().map {
            BeneficiaryDTO(
                    id = it[BeneficiaryTable.id],
                    name = it[BeneficiaryTable.name],
                    studentNumber = it[BeneficiaryTable.student_number],
                    email = it[BeneficiaryTable.email],
                    idCourse = it[BeneficiaryTable.id_course],
                    telephone = it[BeneficiaryTable.telephone]
            )
        }
    }

    fun add(name: String, studentNumber: Int, email: String, idCourse: Int, telephone: String) {
        transaction {
            BeneficiaryTable.insert {
                it[BeneficiaryTable.name] = name
                it[BeneficiaryTable.student_number] = studentNumber
                it[BeneficiaryTable.email] = email
                it[BeneficiaryTable.id_course] = idCourse
                it[BeneficiaryTable.telephone] = telephone
            }
        }
    }
}
