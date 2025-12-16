package com.ipca.dto.Good

import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class GoodUpdateDTO(
    val name: String? = null,
    val category: String? = null,
    val quantity: Int? = null,
    val dateValidity: LocalDate? = null,
    val status: String? = null
)
