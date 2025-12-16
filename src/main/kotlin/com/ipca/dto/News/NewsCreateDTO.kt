package com.ipca.dto.News
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class NewsCreateDTO(
    val title: String,
    val content: String,
    val datePublication: LocalDate,
    val campaignId: Int? = null
)
