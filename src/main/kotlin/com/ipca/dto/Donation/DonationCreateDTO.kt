package com.ipca.dto.Donation

import kotlinx.serialization.Serializable
import kotlinx.datetime.LocalDate

@Serializable
data class DonationCreateDTO(
    val nameDonor: String,
    val type: String,
    val dateDonor: LocalDate,
    val description: String? = null
)
