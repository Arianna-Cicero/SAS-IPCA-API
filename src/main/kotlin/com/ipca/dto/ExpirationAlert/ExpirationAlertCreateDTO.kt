package com.ipca.dto.ExpirationAlert

import kotlinx.serialization.Serializable
import kotlinx.datetime.LocalDate

@Serializable
data class ExpirationAlertCreateDTO(
    val goodId: Int,
    val dateAlert: LocalDate,
    val remainingDays: Int
)
