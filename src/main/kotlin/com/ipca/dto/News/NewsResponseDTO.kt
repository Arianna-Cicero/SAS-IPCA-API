package com.ipca.dto.News

import com.ipca.dto.Campaign.CampaignSummaryDTO
import kotlinx.serialization.Serializable
import kotlinx.datetime.LocalDate

@Serializable
data class NewsResponseDTO(
    val id: Int,
    val title: String,
    val content: String,
    val datePublication: LocalDate,
    val campaign: CampaignSummaryDTO?
)
