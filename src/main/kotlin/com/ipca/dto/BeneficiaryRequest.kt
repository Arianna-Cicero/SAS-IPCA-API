package com.ipca.dto

import kotlinx.serialization.Serializable

@Serializable
data class BeneficiaryRequest(
    val name: String,
    val studentNumber: Int? = null,
    val email: String? = null,
    val idCourse: Int? = null,
    val telephone: String? = null
)
