package com.ipca.dto.Session

import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class SessionResponseDTO(
    val sessionId: String,
    val createdAt: LocalDateTime,
    val expiresAt: LocalDateTime,
    val active: Boolean
)
