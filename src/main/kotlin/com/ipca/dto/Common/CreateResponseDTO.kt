package com.ipca.dto.common

import kotlinx.serialization.Serializable

@Serializable
data class CreateResponseDTO(
    val message: String,
    val id: String? = null
)
