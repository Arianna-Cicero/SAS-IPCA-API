package com.ipca.dto.DeliveryItem

import kotlinx.serialization.Serializable

@Serializable
data class DeliveryItemResponseDTO(
    val goodId: Int,
    val goodName: String,
    val category: String,
    val quantity: Int
)
