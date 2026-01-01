package com.ipca.validation

import com.ipca.models.CollaboratorTable
import com.ipca.dto.ActivityLog.ActivityLogCreateDTO
import com.ipca.exceptions.ValidationException
import com.ipca.exceptions.EntityNotFoundException
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

object ActivityLogValidator {
    
    fun validateCreate(dto: ActivityLogCreateDTO) {
        validateAction(dto.action)
        validateCollaboratorExists(dto.collaboratorId)
    }
    
    private fun validateAction(action: String) {
        val validActions = setOf(
            "CREATE",
            "READ",
            "UPDATE",
            "DELETE",
            "LOGIN",
            "LOGOUT",
            "STATUS_CHANGE",
            "OTHER"
        )
        if (action !in validActions) {
            throw ValidationException(
                "Invalid action. Must be one of: ${validActions.joinToString(", ")}",
                "activity_log"
            )
        }
    }
    
    private fun validateCollaboratorExists(collaboratorId: UUID) {
        val exists = transaction {
            CollaboratorTable.select { CollaboratorTable.id_collaborator eq collaboratorId }.count() > 0
        }
        
        if (!exists) {
            throw EntityNotFoundException("collaborator", collaboratorId.toString())
        }
    }
}
