package com.ipca.dto.Campaign

import kotlinx.serialization.Serializable

@Serializable
data class CampaignSummaryDTO(
    val id: Int,
    val title: String
)
