package com.ipca.dto.Session

import kotlinx.serialization.Serializable

@Serializable
data class SessionResponseDTO(
    val sessionId: String,
    val createdAt: String,
    val expiresAt: String,
    val active: Boolean
)
