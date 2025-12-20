package com.ipca.services

import com.ipca.dto.Beneficiary.BeneficiaryCreateDTO
import com.ipca.dto.Beneficiary.BeneficiaryUpdateDTO
import com.ipca.models.BeneficiaryTable
import com.ipca.models.SchedulingTable
import com.ipca.models.DeliveryTable
import com.ipca.models.DeliveryItemTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object BeneficiaryService {

    fun getAll(): List<BeneficiaryCreateDTO> = transaction {
        BeneficiaryTable.selectAll().map { row ->
            BeneficiaryCreateDTO(
                name = row[BeneficiaryTable.name],
                studentNumber = row[BeneficiaryTable.studentNumber],
                email = row[BeneficiaryTable.email],
                idCourse = row[BeneficiaryTable.idCourse],
                telephone = row[BeneficiaryTable.telephone]
            )
        }
    }

    fun getById(id: Int): BeneficiaryCreateDTO? = transaction {
        BeneficiaryTable
            .select { BeneficiaryTable.studentNumber eq id }
            .singleOrNull()
            ?.let { row ->
                BeneficiaryCreateDTO(
                    name = row[BeneficiaryTable.name],
                    studentNumber = row[BeneficiaryTable.studentNumber],
                    email = row[BeneficiaryTable.email],
                    idCourse = row[BeneficiaryTable.idCourse],
                    telephone = row[BeneficiaryTable.telephone]
                )
            }
    }

    fun create(request: BeneficiaryCreateDTO): Int = transaction {
        BeneficiaryTable.insertAndGetId { row ->
            row[name] = request.name
            row[studentNumber] = request.studentNumber
            row[email] = request.email
            row[idCourse] = request.idCourse
            row[telephone] = request.telephone
        }.value
    }

    fun update(id: Int, request: BeneficiaryUpdateDTO) = transaction {
        BeneficiaryTable.update({ BeneficiaryTable.studentNumber eq id }) { row ->
            request.name?.let { row[BeneficiaryTable.name] = it }
            request.email?.let { row[BeneficiaryTable.email] = it }
            request.telephone?.let { row[BeneficiaryTable.telephone] = it }
            request.idCourse?.let { row[BeneficiaryTable.idCourse] = it }
        }
    }

    fun delete(id: Int) = transaction {
        BeneficiaryTable.deleteWhere { BeneficiaryTable.studentNumber eq id }
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
