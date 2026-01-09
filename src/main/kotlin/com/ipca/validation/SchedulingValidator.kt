package com.ipca.validation

import com.ipca.models.BeneficiaryTable
import com.ipca.models.CollaboratorTable
import com.ipca.dto.Schedulling.SchedulingCreateDTO
import com.ipca.dto.Schedulling.SchedulingUpdateDTO
import com.ipca.exceptions.ValidationException
import com.ipca.exceptions.EntityNotFoundException
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import kotlinx.datetime.LocalDate
import com.ipca.utils.todayKotlinx
import java.util.UUID

object SchedulingValidator {
    
    fun validateCreate(dto: SchedulingCreateDTO) {
        validateDeliveryDate(dto.dateDelivery)
        validateBeneficiaryExists(dto.beneficiaryId)
        validateCollaboratorExists(dto.collaboratorId)
    }
    
    fun validateUpdate(dto: SchedulingUpdateDTO) {
        dto.dateDelivery?.let { validateDeliveryDate(it) }
        dto.collaboratorId?.let { validateCollaboratorExists(it) }
    }
    
    private fun validateDeliveryDate(deliveryDate: LocalDate) {
        if (deliveryDate < todayKotlinx()) {
            throw ValidationException("Delivery date cannot be in the past", "scheduling")
        }
    }
    
    private fun validateBeneficiaryExists(beneficiaryId: Int) {
        val exists = transaction {
            BeneficiaryTable.select { BeneficiaryTable.studentNumber eq beneficiaryId }.count() > 0
        }
        
        if (!exists) {
            throw EntityNotFoundException("beneficiary", beneficiaryId.toString())
        }
    }
    
    // Accept the collaborator id as a String (from DTO) and parse it here.
    private fun validateCollaboratorExists(collaboratorId: String) {
        val uuid = try {
            UUID.fromString(collaboratorId)
        } catch (e: IllegalArgumentException) {
            throw ValidationException("Invalid collaboratorId UUID", "collaboratorId")
        }

        val exists = transaction {
            CollaboratorTable.select { CollaboratorTable.id_collaborator eq uuid }.count() > 0
        }

        if (!exists) {
            throw ValidationException("Collaborator not found", "scheduling")
        }
    }
}
