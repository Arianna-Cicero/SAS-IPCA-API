package com.ipca.dto.ActivityLog

import kotlinx.serialization.Serializable

@Serializable
data class ActivityLogCreateDTO(
    val collaboratorId: String,
    val entityId: Int,
    val recordId: String?,
    val action: String,
    val timestamp: String? = null
)