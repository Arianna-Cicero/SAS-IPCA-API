package com.ipca.dto

import kotlinx.serialization.Serializable

@Serializable
data class BeneficiaryDTO(
    val idBeneficiary: Int,
    val name: String,
    val studentNumber: Int,
    val email: String,
    val idCourse: Int,
    val telephone: String
)