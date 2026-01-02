package com.ipca.dto.Session

import kotlinx.serialization.Serializable

@Serializable
data class SessionCreateDTO(
    val collaboratorId: String,
    val createdAt: String,
    val expiresAt: String
)
