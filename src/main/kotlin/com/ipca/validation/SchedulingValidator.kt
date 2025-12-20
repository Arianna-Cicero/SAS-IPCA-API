package com.ipca.validation

import com.ipca.models.BeneficiaryTable
import com.ipca.models.CollaboratorTable
import com.ipca.dto.Schedulling.SchedulingCreateDTO
import com.ipca.dto.Schedulling.SchedulingUpdateDTO
import com.ipca.exceptions.ValidationException
import com.ipca.exceptions.EntityNotFoundException
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDate

object SchedulingValidator {
    
    fun validateCreate(dto: SchedulingCreateDTO) {
        validateDeliveryDate(dto.dateDelivery)
        validateBeneficiaryExists(dto.beneficiaryId)
        validateCollaboratorExists(dto.collaboratorId.toString())
    }
    
    fun validateUpdate(dto: SchedulingUpdateDTO) {
        if (dto.dateDelivery != null) validateDeliveryDate(dto.dateDelivery)
        if (dto.beneficiaryId != null) validateBeneficiaryExists(dto.beneficiaryId)
        if (dto.collaboratorId != null) validateCollaboratorExists(dto.collaboratorId.toString())
    }
    
    private fun validateDeliveryDate(deliveryDate: LocalDate) {
        if (deliveryDate.isBefore(LocalDate.now())) {
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
    
    private fun validateCollaboratorExists(collaboratorId: String) {
        try {
            val uuid = java.util.UUID.fromString(collaboratorId)
            val exists = transaction {
                CollaboratorTable.select { CollaboratorTable.id_collaborator eq uuid }.count() > 0
            }
            
            if (!exists) {
                throw EntityNotFoundException("collaborator", collaboratorId)
            }
        } catch (e: IllegalArgumentException) {
            throw ValidationException("Invalid collaborator UUID format", "scheduling")
        }
    }
}
