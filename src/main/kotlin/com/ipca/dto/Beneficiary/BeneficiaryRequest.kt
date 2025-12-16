package com.ipca.dto.Beneficiary

import kotlinx.serialization.Serializable

@Serializable
data class BeneficiaryRequest(
    val name: String,
    val studentNumber: Int,
    val email: String,
    val idCourse: Int,
    val telephone: String
)