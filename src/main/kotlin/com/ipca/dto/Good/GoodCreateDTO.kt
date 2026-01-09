package com.ipca.dto.Good

import kotlinx.serialization.Serializable
import kotlinx.datetime.LocalDate

@Serializable
data class GoodCreateDTO(
    val name: String,
    val category: String,
    val quantity: Int,
    val intake: LocalDate,
    val dateValidity: LocalDate,
    val donationId: String
)
