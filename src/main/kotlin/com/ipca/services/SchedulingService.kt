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
import com.ipca.utils.toKotlinx
import com.ipca.utils.toJava

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
                        id = colRow[CollaboratorTable.id_collaborator].toString(),
                        name = colRow[CollaboratorTable.name],
                        email = colRow[CollaboratorTable.email]
                    )
                }

            SchedulingResponseDTO(
                id = row[SchedulingTable.id],
                dateDelivery = row[SchedulingTable.dateDelivery].toKotlinx(),
                status = row[SchedulingTable.status] ?: "pending",
                beneficiary = beneficiary ?: BeneficiarySummaryDTO(0, "", 0),
                collaborator = collaborator ?: CollaboratorSummaryDTO(java.util.UUID.randomUUID().toString(), "", "")
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
                            id = colRow[CollaboratorTable.id_collaborator].toString(),
                            name = colRow[CollaboratorTable.name],
                            email = colRow[CollaboratorTable.email]
                        )
                    }

                SchedulingResponseDTO(
                    id = row[SchedulingTable.id],
                    dateDelivery = row[SchedulingTable.dateDelivery].toKotlinx(),
                    status = row[SchedulingTable.status] ?: "pending",
                    beneficiary = beneficiary ?: BeneficiarySummaryDTO(0, "", 0),
                    collaborator = collaborator ?: CollaboratorSummaryDTO(java.util.UUID.randomUUID().toString(), "", "")
                )
            }
    }

    fun create(request: SchedulingCreateDTO): Int = transaction {
        val insert = SchedulingTable.insert { row ->
            row[idBeneficiary] = request.beneficiaryId
            row[idCollaborator] = java.util.UUID.fromString(request.collaboratorId)
            row[dateDelivery] = request.dateDelivery.toJava()
            row[status] = "pending"
        }
        insert.resultedValues?.first()?.get(SchedulingTable.id)
            ?: error("Failed to insert Scheduling")
    }

    fun update(id: Int, request: SchedulingUpdateDTO) = transaction {
        SchedulingTable.update({ SchedulingTable.id eq id }) { row ->
            request.dateDelivery?.let { d -> row[SchedulingTable.dateDelivery] = d.toJava() }
            request.collaboratorId?.let { c -> row[SchedulingTable.idCollaborator] = java.util.UUID.fromString(c) }
            request.status?.let { s -> row[SchedulingTable.status] = s }
        }
    }

    fun delete(id: Int) = transaction {
        SchedulingTable.deleteWhere { SchedulingTable.id eq id }
    }
}
