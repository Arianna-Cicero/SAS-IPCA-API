package com.ipca.validation

import com.ipca.dto.Campaign.CampaignCreateDTO
import com.ipca.dto.Campaign.CampaignUpdateDTO
import com.ipca.exceptions.ValidationException
import java.time.LocalDate

object CampaignValidator {
    
    fun validateCreate(dto: CampaignCreateDTO) {
        validateTitle(dto.title)
        validateDescription(dto.description)
        validateDateStart(dto.dateStart)
        validateDateEnd(dto.dateEnd)
        validateDateRange(dto.dateStart, dto.dateEnd)
    }
    
    fun validateUpdate(dto: CampaignUpdateDTO) {
        if (dto.title != null) validateTitle(dto.title)
        if (dto.description != null) validateDescription(dto.description)
        if (dto.dateStart != null && dto.dateEnd != null) {
            validateDateStart(dto.dateStart)
            validateDateEnd(dto.dateEnd)
            validateDateRange(dto.dateStart, dto.dateEnd)
        } else {
            if (dto.dateStart != null) validateDateStart(dto.dateStart)
            if (dto.dateEnd != null) validateDateEnd(dto.dateEnd)
        }
    }
    
    private fun validateTitle(title: String) {
        if (title.isBlank()) {
            throw ValidationException("Campaign title cannot be empty", "campaign")
        }
        if (title.length < 3 || title.length > 255) {
            throw ValidationException("Campaign title must be between 3 and 255 characters", "campaign")
        }
    }
    
    private fun validateDescription(description: String) {
        if (description.isBlank()) {
            throw ValidationException("Campaign description cannot be empty", "campaign")
        }
        if (description.length > 2000) {
            throw ValidationException("Campaign description cannot exceed 2000 characters", "campaign")
        }
    }
    
    private fun validateDateStart(dateStart: LocalDate) {
        // dateStart can be in the past (for historical campaigns)
    }
    
    private fun validateDateEnd(dateEnd: LocalDate) {
        // dateEnd can be in the past (for completed campaigns)
    }
    
    private fun validateDateRange(dateStart: LocalDate, dateEnd: LocalDate) {
        if (dateStart.isAfter(dateEnd)) {
            throw ValidationException(
                "Campaign start date must be before or equal to end date",
                "campaign"
            )
        }
    }
}
