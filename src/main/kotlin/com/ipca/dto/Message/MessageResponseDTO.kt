package com.ipca.dto.Message

import kotlinx.serialization.Serializable

@Serializable
data class MessageResponseDTO(
    val id: Int,
    val name: String,
    val email: String,
    val category: String,
    val message: String
)
