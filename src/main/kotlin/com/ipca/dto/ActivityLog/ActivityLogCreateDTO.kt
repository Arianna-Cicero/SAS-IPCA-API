package com.ipca.dto.ActivityLog

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class ActivityLogCreateDTO(
    val collaboratorId: UUID,
    val entityId: Int,
    val recordId: String?,
    val action: String
)