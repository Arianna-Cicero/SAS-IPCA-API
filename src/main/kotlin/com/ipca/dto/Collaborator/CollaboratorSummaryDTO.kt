package com.ipca.dto.Collaborator

import kotlinx.serialization.Serializable

@Serializable
data class CollaboratorSummaryDTO(
    val id: String,
    val name: String,
    val email: String
)
