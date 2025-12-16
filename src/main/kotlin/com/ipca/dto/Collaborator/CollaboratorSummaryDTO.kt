package com.ipca.dto.Collaborator

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class CollaboratorSummaryDTO(
    val id: UUID,
    val name: String,
    val email: String
)
