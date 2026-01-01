package com.ipca.validation

import com.ipca.models.GoodTable
import com.ipca.dto.DeliveryItem.DeliveryItemCreateDTO
import com.ipca.exceptions.ValidationException
import com.ipca.exceptions.EntityNotFoundException
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

object DeliveryItemValidator {
    
    fun validateCreate(dto: DeliveryItemCreateDTO) {
        validateGoodExists(dto.goodId)
        validateQuantity(dto.quantity)
    }
    
    private fun validateGoodExists(goodId: Int) {
        val exists = transaction {
            GoodTable.select { GoodTable.id eq goodId }.count() > 0
        }
        
        if (!exists) {
            throw EntityNotFoundException("good", goodId.toString())
        }
    }
    
    private fun validateQuantity(quantity: Int) {
        if (quantity <= 0) {
            throw ValidationException("Quantity must be greater than 0", "delivery_item")
        }
    }
}
