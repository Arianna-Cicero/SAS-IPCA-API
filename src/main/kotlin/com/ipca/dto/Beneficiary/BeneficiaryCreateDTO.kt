package com.ipca.dto.Beneficiary

import kotlinx.serialization.Serializable

@Serializable
data class BeneficiaryCreateDTO(
    val name: String,
    val studentNumber: Int,
    val email: String,
    val telephone: String,
    val idCourse: Int
)