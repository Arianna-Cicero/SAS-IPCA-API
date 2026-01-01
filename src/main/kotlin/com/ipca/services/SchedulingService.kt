package com.ipca.services

import com.ipca.dto.Schedulling.SchedulingCreateDTO
import com.ipca.dto.Schedulling.SchedulingResponseDTO
import com.ipca.dto.Schedulling.SchedulingUpdateDTO
import com.ipca.dto.Beneficiary.BeneficiarySummaryDTO
import com.ipca.dto.Collaborator.CollaboratorSummaryDTO
import com.ipca.models.SchedulingTable
import com.ipca.models.BeneficiaryTable
import com.ipca.models.CollaboratorTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

object SchedulingService {

    fun getAll(): List<SchedulingResponseDTO> = transaction {
        SchedulingTable.selectAll().map { row ->
            val beneficiaryId = row[SchedulingTable.idBeneficiary]
            val collaboratorId = row[SchedulingTable.idCollaborator]

            val beneficiary = BeneficiaryTable
                .select { BeneficiaryTable.studentNumber eq beneficiaryId }
                .singleOrNull()
                ?.let { benRow ->
                    BeneficiarySummaryDTO(
                        id = benRow[BeneficiaryTable.studentNumber],
                        name = benRow[BeneficiaryTable.name],
                        studentNumber = benRow[BeneficiaryTable.studentNumber]
                    )
                }

            val collaborator = CollaboratorTable
                .select { CollaboratorTable.id_collaborator eq collaboratorId }
                .singleOrNull()
                ?.let { colRow ->
                    CollaboratorSummaryDTO(
                        id = colRow[CollaboratorTable.id_collaborator],
                        name = colRow[CollaboratorTable.name],
                        email = colRow[CollaboratorTable.email]
                    )
                }

            SchedulingResponseDTO(
                id = row[SchedulingTable.id],
                dateDelivery = row[SchedulingTable.dateDelivery],
                status = row[SchedulingTable.status] ?: "pending",
                beneficiary = beneficiary ?: BeneficiarySummaryDTO(0, "", 0),
                collaborator = collaborator ?: CollaboratorSummaryDTO(java.util.UUID.randomUUID(), "", "")
            )
        }
    }

    fun getById(id: Int): SchedulingResponseDTO? = transaction {
        SchedulingTable
            .select { SchedulingTable.id eq id }
            .singleOrNull()
            ?.let { row ->
                val beneficiaryId = row[SchedulingTable.idBeneficiary]
                val collaboratorId = row[SchedulingTable.idCollaborator]

                val beneficiary = BeneficiaryTable
                    .select { BeneficiaryTable.studentNumber eq beneficiaryId }
                    .singleOrNull()
                    ?.let { benRow ->
                        BeneficiarySummaryDTO(
                            id = benRow[BeneficiaryTable.studentNumber],
                            name = benRow[BeneficiaryTable.name],
                            studentNumber = benRow[BeneficiaryTable.studentNumber]
                        )
                    }

                val collaborator = CollaboratorTable
                    .select { CollaboratorTable.id_collaborator eq collaboratorId }
                    .singleOrNull()
                    ?.let { colRow ->
                        CollaboratorSummaryDTO(
                            id = colRow[CollaboratorTable.id_collaborator],
                            name = colRow[CollaboratorTable.name],
                            email = colRow[CollaboratorTable.email]
                        )
                    }

                SchedulingResponseDTO(
                    id = row[SchedulingTable.id],
                    dateDelivery = row[SchedulingTable.dateDelivery],
                    status = row[SchedulingTable.status] ?: "pending",
                    beneficiary = beneficiary ?: BeneficiarySummaryDTO(0, "", 0),
                    collaborator = collaborator ?: CollaboratorSummaryDTO(java.util.UUID.randomUUID(), "", "")
                )
            }
    }

    fun create(request: SchedulingCreateDTO): Int = transaction {
        val insert = SchedulingTable.insert { row ->
            row[idBeneficiary] = request.beneficiaryId
            row[idCollaborator] = request.collaboratorId
            row[dateDelivery] = request.dateDelivery
            row[status] = "pending"
        }
        insert.resultedValues?.first()?.get(SchedulingTable.id)
            ?: error("Failed to insert Scheduling")
    }

    fun update(id: Int, request: SchedulingUpdateDTO) = transaction {
        SchedulingTable.update({ SchedulingTable.id eq id }) { row ->
            request.dateDelivery?.let { row[SchedulingTable.dateDelivery] = it }
            request.collaboratorId?.let { row[SchedulingTable.idCollaborator] = it }
            request.status?.let { row[SchedulingTable.status] = it }
        }
    }

    fun delete(id: Int) = transaction {
        SchedulingTable.deleteWhere { SchedulingTable.id eq id }
    }
}
