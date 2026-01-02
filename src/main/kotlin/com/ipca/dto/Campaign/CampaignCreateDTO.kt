package com.ipca.dto.Campaign

import kotlinx.serialization.Serializable
import kotlinx.datetime.LocalDate

@Serializable
data class CampaignCreateDTO(
    val title: String,
    val description: String,
    val dateStart: LocalDate,
    val dateEnd: LocalDate
)
