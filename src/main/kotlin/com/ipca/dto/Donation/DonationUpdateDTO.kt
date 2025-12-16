package com.ipca.dto.Donation

import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class DonationUpdateDTO(
    val nameDonor: String? = null,
    val type: String? = null,
    val dateDonor: LocalDate? = null,
    val description: String? = null
)
