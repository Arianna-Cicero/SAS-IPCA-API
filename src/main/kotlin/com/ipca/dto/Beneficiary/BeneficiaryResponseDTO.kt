package com.ipca.dto.Beneficiary

import kotlinx.serialization.Serializable

@Serializable
data class BeneficiaryResponseDTO(
    val id: Int,
    val name: String,
    val studentNumber: Int,
    val email: String,
    val telephone: String,
    val course: CourseDTO
)
