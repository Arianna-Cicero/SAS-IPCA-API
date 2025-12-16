package com.ipca.dto.Schedulling

import com.ipca.dto.Beneficiary.BeneficiarySummaryDTO
import com.ipca.dto.Collaborator.CollaboratorSummaryDTO
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.util.UUID

@Serializable
data class SchedulingResponseDTO(
    val id: Int,
    val dateDelivery: LocalDate,
    val status: String,
    val beneficiary: BeneficiarySummaryDTO,
    val collaborator: CollaboratorSummaryDTO
)
