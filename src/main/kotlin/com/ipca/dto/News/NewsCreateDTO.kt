package com.ipca.dto.News
import kotlinx.serialization.Serializable
import kotlinx.datetime.LocalDate

@Serializable
data class NewsCreateDTO(
    val title: String,
    val content: String,
    val datePublication: LocalDate,
    val campaignId: Int? = null
)
