package com.ipca.dto.Beneficiary
import kotlinx.serialization.Serializable

@Serializable
data class BeneficiarySummaryDTO(
    val id: Int,
    val name: String,
    val studentNumber: Int
)
