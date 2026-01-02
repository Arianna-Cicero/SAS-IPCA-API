package com.ipca.dto.Schedulling

import kotlinx.serialization.Serializable
import kotlinx.datetime.LocalDate

@Serializable
data class SchedulingCreateDTO(
    val beneficiaryId: Int,
    val collaboratorId: String,
    val dateDelivery: LocalDate
)
