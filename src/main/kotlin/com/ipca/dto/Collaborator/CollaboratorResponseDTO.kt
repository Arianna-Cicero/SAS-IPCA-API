package com.ipca.dto.Collaborator

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class CollaboratorResponseDTO(
    val id: UUID,
    val name: String,
    val email: String,
    val profile: String,
    val active: Boolean
)
