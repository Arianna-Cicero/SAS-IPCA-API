package com.ipca.dto.Delivery

import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class DeliveryCreateDTO(
    val schedulingId: Int,
    val dateDelivery: LocalDate
)
