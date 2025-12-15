package com.ipca.services

import com.ipca.dto.BeneficiaryDTO
import com.ipca.dto.BeneficiaryRequest
import com.ipca.models.BeneficiaryTable
import com.ipca.models.SchedulingTable
import com.ipca.models.DeliveryTable
import com.ipca.models.DeliveryItemTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object BeneficiaryService {

    fun getAll(): List<BeneficiaryDTO> = transaction {
        BeneficiaryTable.selectAll().map { row ->
            BeneficiaryDTO(
                idBeneficiary = row[BeneficiaryTable.id].value,
                name = row[BeneficiaryTable.name],
                studentNumber = row[BeneficiaryTable.studentNumber],
                email = row[BeneficiaryTable.email],
                idCourse = row[BeneficiaryTable.idCourse],
                telephone = row[BeneficiaryTable.telephone]
            )
        }
    }

    fun getById(id: Int): BeneficiaryDTO? = transaction {
        BeneficiaryTable
            .select { BeneficiaryTable.id eq id }
            .singleOrNull()
            ?.let { row ->
                BeneficiaryDTO(
                    idBeneficiary = row[BeneficiaryTable.id].value,
                    name = row[BeneficiaryTable.name],
                    studentNumber = row[BeneficiaryTable.studentNumber],
                    email = row[BeneficiaryTable.email],
                    idCourse = row[BeneficiaryTable.idCourse],
                    telephone = row[BeneficiaryTable.telephone]
                )
            }
    }

    fun create(request: BeneficiaryRequest): Int = transaction {
        BeneficiaryTable.insertAndGetId { row ->
            row[name] = request.name
            row[studentNumber] = request.studentNumber
            row[email] = request.email
            row[idCourse] = request.idCourse
            row[telephone] = request.telephone
        }.value
    }



    fun getSchedulingForBeneficiary(beneficiaryId: Int): List<Map<String, Any?>> = transaction {
        SchedulingTable
            .select { SchedulingTable.idBeneficiary eq beneficiaryId }
            .map { row ->
                mapOf(
                    "id" to row[SchedulingTable.id],
                    "dateDelivery" to row[SchedulingTable.dateDelivery],
                    "status" to row[SchedulingTable.status],
                    "idCollaborator" to row[SchedulingTable.idCollaborator]
                )
            }
    }


    fun getDeliveriesForBeneficiary(beneficiaryId: Int): List<Map<String, Any?>> = transaction {

        // first find all schedulings from this beneficiary
        val schedulings = SchedulingTable
            .select { SchedulingTable.idBeneficiary eq beneficiaryId }
            .map { it[SchedulingTable.id] }

        // now get deliveries for those schedulings
        DeliveryTable
            .select { DeliveryTable.idScheduling inList schedulings }
            .map { deliveryRow ->

                val deliveryId = deliveryRow[DeliveryTable.id]

                // get items for this delivery
                val items = DeliveryItemTable
                    .select { DeliveryItemTable.idDelivery eq deliveryId }
                    .map { itemRow ->
                        mapOf(
                            "goodId" to itemRow[DeliveryItemTable.idGood],
                            "quantity" to itemRow[DeliveryItemTable.quantity]
                        )
                    }

                mapOf(
                    "id" to deliveryId,
                    "dateDelivery" to deliveryRow[DeliveryTable.dateDelivery],
                    "status" to deliveryRow[DeliveryTable.status],
                    "items" to items
                )
            }
    }
}
