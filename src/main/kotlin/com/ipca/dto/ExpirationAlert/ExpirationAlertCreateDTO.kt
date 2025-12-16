package com.ipca.dto.ExpirationAlert

import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class ExpirationAlertCreateDTO(
    val goodId: Int,
    val dateAlert: LocalDate,
    val remainingDays: Int
)
