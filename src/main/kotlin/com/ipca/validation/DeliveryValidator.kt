package com.ipca.validation

import com.ipca.models.SchedulingTable
import com.ipca.dto.Delivery.DeliveryCreateDTO
import com.ipca.exceptions.ValidationException
import com.ipca.exceptions.EntityNotFoundException
import com.ipca.exceptions.InvalidStatusTransitionException
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

object DeliveryValidator {
    
    private val VALID_STATUSES = setOf("PENDING", "COMPLETED", "CANCELLED")
    private val STATUS_TRANSITIONS = mapOf(
        "PENDING" to setOf("COMPLETED", "CANCELLED"),
        "COMPLETED" to setOf("CANCELLED"),
        "CANCELLED" to setOf()
    )
    
    fun validateCreate(dto: DeliveryCreateDTO) {
        validateSchedulingExists(dto.schedulingId)
    }
    
    fun validateStatusUpdate(newStatus: String, currentStatus: String) {
        validateStatus(newStatus)
        validateStatusTransition(currentStatus, newStatus)
    }
    
    private fun validateStatus(status: String) {
        if (status !in VALID_STATUSES) {
            throw ValidationException(
                "Invalid status. Must be one of: ${VALID_STATUSES.joinToString(", ")}",
                "delivery"
            )
        }
    }
    
    private fun validateStatusTransition(fromStatus: String, toStatus: String) {
        val allowedTransitions = STATUS_TRANSITIONS[fromStatus] ?: setOf()
        if (toStatus !in allowedTransitions) {
            throw InvalidStatusTransitionException(
                entityType = "delivery",
                field = "status",
                currentStatus = fromStatus,
                targetStatus = toStatus
            )
        }
    }
    
    private fun validateSchedulingExists(schedulingId: Int) {
        val exists = transaction {
            SchedulingTable.select { SchedulingTable.id eq schedulingId }.count() > 0
        }
        
        if (!exists) {
            throw EntityNotFoundException("scheduling", schedulingId.toString())
        }
    }
}
