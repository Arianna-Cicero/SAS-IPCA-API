package com.ipca.validation

import com.ipca.dto.Entity.EntityCreateDTO
import com.ipca.dto.Entity.EntityUpdateDTO
import com.ipca.exceptions.ValidationException
import java.time.LocalDate

object EntityValidator {
    
    fun validateCreate(dto: EntityCreateDTO) {
        validateName(dto.name)
    }
    
    fun validateUpdate(dto: EntityUpdateDTO) {
        if (dto.name != null) validateName(dto.name)
    }
    
    private fun validateName(name: String) {
        if (name.isBlank()) {
            throw ValidationException("Entity name cannot be empty", "entity")
        }
        if (name.length < 3 || name.length > 255) {
            throw ValidationException("Entity name must be between 3 and 255 characters", "entity")
        }
    }
}
