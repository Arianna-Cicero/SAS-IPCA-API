package com.ipca.dto.ExpirationAlert

import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class ExpirationAlertResponseDTO(
    val id: Int,
    val goodId: Int,
    val goodName: String,
    val category: String,
    val dateValidity: LocalDate,
    val dateAlert: LocalDate,
    val remainingDays: Int,
    val resolved: Boolean
)
