package com.ipca.dto.Delivery

import kotlinx.serialization.Serializable

@Serializable
data class DeliveryUpdateStatusDTO(
    val status: String
)
