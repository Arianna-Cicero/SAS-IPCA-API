package com.ipca.dto.ActivityLog

import kotlinx.serialization.Serializable
import java.time.LocalDateTime
import java.util.UUID

@Serializable
data class ActivityLogResponseDTO(
    val id: Int,
    val collaboratorId: UUID,
    val entityId: Int,
    val entityName: String,
    val recordId: String?,
    val action: String,
    val timestamp: LocalDateTime
)