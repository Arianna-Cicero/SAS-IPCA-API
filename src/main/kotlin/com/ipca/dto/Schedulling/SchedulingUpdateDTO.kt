package com.ipca.dto.Schedulling

import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.util.UUID

@Serializable
data class SchedulingUpdateDTO(
    val dateDelivery: LocalDate? = null,
    val collaboratorId: UUID? = null,
    val status: String? = null
)
