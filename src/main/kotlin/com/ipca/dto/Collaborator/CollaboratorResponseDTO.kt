package com.ipca.dto.Collaborator

import kotlinx.serialization.Serializable

@Serializable
data class CollaboratorResponseDTO(
    val id: String,
    val name: String,
    val email: String,
    val profile: String,
    val active: Boolean
)
