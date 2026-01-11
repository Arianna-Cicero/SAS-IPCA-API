package com.ipca.dto.Delivery

import com.ipca.dto.DeliveryItem.DeliveryItemResponseDTO
import kotlinx.serialization.Serializable
import kotlinx.datetime.LocalDate

@Serializable
data class DeliveryResponseDTO(
    val id: String,
    val schedulingId: Int,
    val beneficiaryName: String,
    val dateDelivery: LocalDate,
    val status: String,
    val items: List<DeliveryItemResponseDTO>
)
