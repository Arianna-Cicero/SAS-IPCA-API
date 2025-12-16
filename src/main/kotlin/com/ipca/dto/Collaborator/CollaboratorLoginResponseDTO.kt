package com.ipca.dto.Collaborator

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class CollaboratorLoginResponseDTO(
    val collaborator: CollaboratorResponseDTO,
    val sessionId: String,
    val expiresAt: String
)
