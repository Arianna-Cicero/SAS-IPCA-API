package com.ipca.dto.Campaign

import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class CampaignCreateDTO(
    val title: String,
    val description: String,
    val dateStart: LocalDate,
    val dateEnd: LocalDate
)
