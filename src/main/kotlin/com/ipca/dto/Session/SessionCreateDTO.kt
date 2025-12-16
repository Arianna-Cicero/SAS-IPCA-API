package com.ipca.dto.Session

import kotlinx.serialization.Serializable
import java.time.LocalDateTime
import java.util.UUID

@Serializable
data class SessionCreateDTO(
    val collaboratorId: UUID,
    val createdAt: LocalDateTime,
    val expiresAt: LocalDateTime
)
