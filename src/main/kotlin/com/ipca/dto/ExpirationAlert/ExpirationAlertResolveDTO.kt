package com.ipca.dto.ExpirationAlert

import kotlinx.serialization.Serializable

@Serializable
data class ExpirationAlertResolveDTO(
    val resolved: Boolean = true
)
