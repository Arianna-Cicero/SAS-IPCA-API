package com.ipca.dto.Campaign

import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class CampaignResponseDTO(
    val id: Int,
    val title: String,
    val description: String,
    val dateStart: LocalDate,
    val dateEnd: LocalDate,
    val totalCollected: Int,
    val active: Boolean
)
