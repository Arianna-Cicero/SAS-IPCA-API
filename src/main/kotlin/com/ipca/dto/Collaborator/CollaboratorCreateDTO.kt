package com.ipca.dto.Collaborator

import kotlinx.serialization.Serializable

@Serializable
data class CollaboratorCreateDTO(
    val name: String,
    val email: String,
    val password: String,
    val profile: String
)
