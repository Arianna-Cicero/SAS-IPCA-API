package com.ipca.validation

import com.ipca.models.GoodTable
import com.ipca.dto.ExpirationAlert.ExpirationAlertCreateDTO
import com.ipca.exceptions.ValidationException
import com.ipca.exceptions.EntityNotFoundException
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

object ExpirationAlertValidator {
    
    fun validateCreate(dto: ExpirationAlertCreateDTO) {
        validateGoodExists(dto.goodId)
        validateRemainingDays(dto.remainingDays)
    }
    
    // No update DTO exists; update validation handled per-route if needed
    
    private fun validateGoodExists(goodId: Int) {
        val exists = transaction {
            GoodTable.select { GoodTable.id eq goodId }.count() > 0
        }
        
        if (!exists) {
            throw EntityNotFoundException("good", goodId.toString())
        }
    }
    
    private fun validateRemainingDays(remainingDays: Int) {
        if (remainingDays < 0) {
            throw ValidationException("Remaining days cannot be negative", "expiration_alert")
        }
        if (remainingDays > 365) {
            throw ValidationException("Remaining days cannot exceed 365", "expiration_alert")
        }
    }
}
