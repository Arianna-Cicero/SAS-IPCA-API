package com.ipca.dto.Good

import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class GoodCreateDTO(
    val name: String,
    val category: String,
    val quantity: Int,
    val intake: LocalDate,
    val dateValidity: LocalDate,
    val donationId: String
)
