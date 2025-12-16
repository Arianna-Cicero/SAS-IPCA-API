package com.ipca.dto.Entity

import kotlinx.serialization.Serializable

@Serializable
data class EntityResponseDTO(
    val id: Int,
    val name: String
)
