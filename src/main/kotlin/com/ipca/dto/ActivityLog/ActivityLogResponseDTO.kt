package com.ipca.dto.ActivityLog

import kotlinx.serialization.Serializable

@Serializable
data class ActivityLogResponseDTO(
    val id: Int,
    val collaboratorId: String,
    val entityId: Int,
    val entityName: String,
    val recordId: String?,
    val action: String,
    val timestamp: String
)