package com.ipca.dto.Session

import kotlinx.serialization.Serializable

@Serializable
data class SessionValidateResponseDTO(
    val valid: Boolean
)
