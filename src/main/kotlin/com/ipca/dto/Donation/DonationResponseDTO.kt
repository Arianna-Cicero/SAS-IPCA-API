package com.ipca.dto.Donation

import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class DonationResponseDTO(
    val id: String,
    val nameDonor: String,
    val type: String,
    val dateDonor: LocalDate,
    val description: String?
)
