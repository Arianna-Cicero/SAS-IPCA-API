package com.ipca.validation

import com.ipca.models.GoodTable
import com.ipca.dto.Donation.DonationCreateDTO
import com.ipca.dto.Donation.DonationUpdateDTO
import com.ipca.exceptions.ValidationException
import com.ipca.exceptions.EntityNotFoundException
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

object DonationValidator {
    
    fun validateCreate(dto: DonationCreateDTO) {
        validateName(dto.nameDonor)
        validateType(dto.type)
    }
    
    fun validateUpdate(dto: DonationUpdateDTO) {
        if (dto.nameDonor != null) validateName(dto.nameDonor)
        if (dto.type != null) validateType(dto.type)
    }
    
    private fun validateName(name: String) {
        if (name.isBlank()) {
            throw ValidationException("Donor name cannot be empty", "donation")
        }
        if (name.length < 3 || name.length > 255) {
            throw ValidationException("Donor name must be between 3 and 255 characters", "donation")
        }
    }
    
    private fun validateType(type: String) {
        val validTypes = setOf("MONETARY", "GOODS", "SERVICES")
        if (type !in validTypes) {
            throw ValidationException(
                "Invalid type. Must be one of: ${validTypes.joinToString(", ")}",
                "donation"
            )
        }
    }
}
