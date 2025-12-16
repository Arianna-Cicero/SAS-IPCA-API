package com.ipca.dto.Delivery

import com.ipca.dto.DeliveryItem.DeliveryItemResponseDTO
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class DeliveryResponseDTO(
    val id: String,
    val schedulingId: Int,
    val dateDelivery: LocalDate,
    val status: String,
    val items: List<DeliveryItemResponseDTO>
)
