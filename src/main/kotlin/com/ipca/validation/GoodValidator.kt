package com.ipca.validation

import com.ipca.dto.Good.GoodCreateDTO
import com.ipca.dto.Good.GoodUpdateDTO
import com.ipca.exceptions.ValidationException
import kotlinx.datetime.LocalDate
import com.ipca.utils.todayKotlinx

object GoodValidator {
    
    fun validateCreate(dto: GoodCreateDTO) {
        validateName(dto.name)
        validateCategory(dto.category)
        validateQuantity(dto.quantity)
        validateIntakeDate(dto.intake)
        validateValidityDate(dto.dateValidity)
        validateDateRange(dto.intake, dto.dateValidity)
    }
    
    fun validateUpdate(dto: GoodUpdateDTO) {
        if (dto.name != null) validateName(dto.name)
        if (dto.category != null) validateCategory(dto.category)
        if (dto.quantity != null) validateQuantity(dto.quantity)
    }
    
    private fun validateName(name: String) {
        if (name.isBlank()) {
            throw ValidationException("Good name cannot be empty", "good")
        }
        if (name.length < 3 || name.length > 255) {
            throw ValidationException("Good name must be between 3 and 255 characters", "good")
        }
    }
    
    private fun validateCategory(category: String) {
        val validCategories = setOf(
            "FOOD",
            "CLOTHING",
            "HYGIENE",
            "EDUCATIONAL_MATERIAL",
            "MEDICAL_SUPPLIES",
            "OTHER"
        )
        if (category !in validCategories) {
            throw ValidationException(
                "Invalid category. Must be one of: ${validCategories.joinToString(", ")}",
                "good"
            )
        }
    }
    
    private fun validateQuantity(quantity: Int) {
        if (quantity < 0) {
            throw ValidationException("Quantity cannot be negative", "good")
        }
    }
    
    private fun validateIntakeDate(intakeDate: LocalDate) {
        if (intakeDate > todayKotlinx()) {
            throw ValidationException("Intake date cannot be in the future", "good")
        }
    }
    
    private fun validateValidityDate(validityDate: LocalDate) {
        if (validityDate < todayKotlinx()) {
            throw ValidationException("Validity date must be in the future or today", "good")
        }
    }
    
    private fun validateDateRange(intakeDate: LocalDate, validityDate: LocalDate) {
        if (intakeDate > validityDate) {
            throw ValidationException(
                "Intake date must be before or equal to validity date",
                "good"
            )
        }
    }
}
