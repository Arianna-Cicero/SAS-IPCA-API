package com.ipca.services

import com.ipca.dto.Beneficiary.BeneficiaryCreateDTO
import com.ipca.dto.Beneficiary.BeneficiaryResponseDTO
import com.ipca.dto.Beneficiary.BeneficiaryUpdateDTO
import com.ipca.dto.Beneficiary.CourseDTO
import com.ipca.models.BeneficiaryTable
import com.ipca.models.CourseTable
import com.ipca.models.SchedulingTable
import com.ipca.models.DeliveryTable
import com.ipca.models.DeliveryItemTable
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

object BeneficiaryService {

    fun getAll(): List<BeneficiaryResponseDTO> = transaction {
        (BeneficiaryTable innerJoin CourseTable).selectAll().map { row ->
            BeneficiaryResponseDTO(
                id = row[BeneficiaryTable.studentNumber],
                name = row[BeneficiaryTable.name],
                studentNumber = row[BeneficiaryTable.studentNumber],
                email = row[BeneficiaryTable.email],
                telephone = row[BeneficiaryTable.telephone],
                course = CourseDTO(
                    id = row[CourseTable.id],
                    name = row[CourseTable.name]
                )
            )
        }
    }

    fun getById(id: Int): BeneficiaryResponseDTO? = transaction {
        (BeneficiaryTable innerJoin CourseTable)
            .select { BeneficiaryTable.studentNumber eq id }
            .singleOrNull()
            ?.let { row ->
                BeneficiaryResponseDTO(
                    id = row[BeneficiaryTable.studentNumber],
                    name = row[BeneficiaryTable.name],
                    studentNumber = row[BeneficiaryTable.studentNumber],
                    email = row[BeneficiaryTable.email],
                    telephone = row[BeneficiaryTable.telephone],
                    course = CourseDTO(
                        id = row[CourseTable.id],
                        name = row[CourseTable.name]
                    )
                )
            }
    }

    fun create(request: BeneficiaryCreateDTO): Int = transaction {
        BeneficiaryTable.insert { row ->
            row[name] = request.name
            row[studentNumber] = request.studentNumber
            row[email] = request.email
            row[idCourse] = request.idCourse
            row[telephone] = request.telephone
        }
        request.studentNumber
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

    @Serializable
    data class BeneficiarySchedulingDTO(
        val id: Int,
        val dateDelivery: String,
        val status: String,
        val idCollaborator: String
    )

    fun getSchedulingForBeneficiary(beneficiaryId: Int): List<BeneficiarySchedulingDTO> = transaction {
        SchedulingTable
            .select { SchedulingTable.idBeneficiary eq beneficiaryId }
            .map { row ->
                BeneficiarySchedulingDTO(
                    id = row[SchedulingTable.id],
                    dateDelivery = row[SchedulingTable.dateDelivery].toString(),
                    status = row[SchedulingTable.status] ?: "",
                    idCollaborator = row[SchedulingTable.idCollaborator].toString()
                )
            }
    }

    @Serializable
    data class DeliveryItemDTO(
        val goodId: Int,
        val quantity: Int
    )

    @Serializable
    data class BeneficiaryDeliveryDTO(
        val id: String,
        val dateDelivery: String,
        val status: String,
        val items: List<DeliveryItemDTO>
    )

    fun getDeliveriesForBeneficiary(beneficiaryId: Int): List<BeneficiaryDeliveryDTO> = transaction {
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
                        DeliveryItemDTO(
                            goodId = itemRow[DeliveryItemTable.idGood],
                            quantity = itemRow[DeliveryItemTable.quantity]
                        )
                    }

                BeneficiaryDeliveryDTO(
                    id = deliveryId.toString(),
                    dateDelivery = deliveryRow[DeliveryTable.dateDelivery].toString(),
                    status = deliveryRow[DeliveryTable.status],
                    items = items
                )
            }
    }
}
