package com.ipca.dto.DeliveryItem

import kotlinx.serialization.Serializable

@Serializable
data class DeliveryItemCreateDTO(
    val goodId: Int,
    val quantity: Int
)
