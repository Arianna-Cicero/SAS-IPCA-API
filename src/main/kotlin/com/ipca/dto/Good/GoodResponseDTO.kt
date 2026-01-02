package com.ipca.dto.Good

import com.ipca.dto.Donation.DonationResponseDTO
import kotlinx.serialization.Serializable
import kotlinx.datetime.LocalDate

@Serializable
data class GoodResponseDTO(
    val id: Int,
    val name: String,
    val category: String,
    val quantity: Int,
    val intake: LocalDate,
    val dateValidity: LocalDate,
    val status: String,
    val donation: DonationResponseDTO
)
