package com.ipca.dto.News

import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class NewsUpdateDTO(
    val title: String? = null,
    val content: String? = null,
    val datePublication: LocalDate? = null,
    val campaignId: Int? = null
)
