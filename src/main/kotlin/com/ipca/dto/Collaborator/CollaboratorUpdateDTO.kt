package com.ipca.dto.Collaborator

import kotlinx.serialization.Serializable

@Serializable
data class CollaboratorUpdateDTO(
    val name: String? = null,
    val email: String? = null,
    val profile: String? = null,
    val active: Boolean? = null
)
