package com.ipca.dto.Beneficiary

import kotlinx.serialization.Serializable

@Serializable
data class BeneficiaryUpdateDTO(
    val name: String? = null,
    val studentNumber: Int? = null,
    val email: String? = null,
    val telephone: String? = null,
    val courseId: Int? = null
)
