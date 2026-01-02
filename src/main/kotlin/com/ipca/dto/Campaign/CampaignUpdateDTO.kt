package com.ipca.dto.Campaign

import kotlinx.serialization.Serializable
import kotlinx.datetime.LocalDate

@Serializable
data class CampaignUpdateDTO(
    val title: String? = null,
    val description: String? = null,
    val dateStart: LocalDate? = null,
    val dateEnd: LocalDate? = null
)
