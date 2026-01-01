package com.ipca.validation

import com.ipca.models.CollaboratorTable
import com.ipca.dto.Collaborator.CollaboratorCreateDTO
import com.ipca.dto.Collaborator.CollaboratorUpdateDTO
import com.ipca.exceptions.ValidationException
import com.ipca.exceptions.EntityAlreadyExistsException
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

object CollaboratorValidator {
    
    private val EMAIL_PATTERN = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
    
    fun validateCreate(dto: CollaboratorCreateDTO) {
        validateName(dto.name)
        validateEmail(dto.email, isUpdate = false)
        validatePassword(dto.password)
        validateProfile(dto.profile)
    }
    
    fun validateUpdate(dto: CollaboratorUpdateDTO, existingEmail: String? = null) {
        if (dto.name != null) validateName(dto.name)
        if (dto.email != null) validateEmail(dto.email, isUpdate = true, existingEmail = existingEmail)
        if (dto.profile != null) validateProfile(dto.profile)
    }
    
    private fun validateName(name: String) {
        if (name.isBlank()) {
            throw ValidationException("Collaborator name cannot be empty", "collaborator")
        }
        if (name.length < 3 || name.length > 255) {
            throw ValidationException("Collaborator name must be between 3 and 255 characters", "collaborator")
        }
    }
    
    private fun validateEmail(email: String, isUpdate: Boolean = false, existingEmail: String? = null) {
        if (!EMAIL_PATTERN.matches(email)) {
            throw ValidationException("Invalid email format: $email", "collaborator")
        }
        
        // If updating with the same email, skip uniqueness check
        if (isUpdate && email == existingEmail) {
            return
        }
        
        // Check uniqueness
        val exists = transaction {
            CollaboratorTable.select { CollaboratorTable.email eq email }.count() > 0
        }
        
        if (exists) {
            throw EntityAlreadyExistsException("collaborator", "email", email)
        }
    }
    
    private fun validatePassword(password: String) {
        if (password.length < 8) {
            throw ValidationException("Password must be at least 8 characters long", "collaborator")
        }
        
        // Check for complexity: at least one uppercase, one lowercase, one digit
        val hasUppercase = password.any { it.isUpperCase() }
        val hasLowercase = password.any { it.isLowerCase() }
        val hasDigit = password.any { it.isDigit() }
        
        if (!hasUppercase || !hasLowercase || !hasDigit) {
            throw ValidationException(
                "Password must contain at least one uppercase letter, one lowercase letter, and one digit",
                "collaborator"
            )
        }
    }
    
    private fun validateProfile(profile: String) {
        val validProfiles = setOf("ADMIN", "COORDINATOR", "VOLUNTEER")
        if (profile !in validProfiles) {
            throw ValidationException(
                "Invalid profile. Must be one of: ${validProfiles.joinToString(", ")}",
                "collaborator"
            )
        }
    }
}
