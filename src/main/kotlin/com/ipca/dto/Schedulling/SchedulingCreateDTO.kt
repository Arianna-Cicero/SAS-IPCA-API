package com.ipca.dto.Schedulling

import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.util.UUID

@Serializable
data class SchedulingCreateDTO(
    val beneficiaryId: Int,
    val collaboratorId: UUID,
    val dateDelivery: LocalDate
)
