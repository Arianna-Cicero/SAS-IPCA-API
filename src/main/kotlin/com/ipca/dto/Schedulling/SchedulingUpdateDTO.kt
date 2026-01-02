package com.ipca.dto.Schedulling

import kotlinx.serialization.Serializable
import kotlinx.datetime.LocalDate

@Serializable
data class SchedulingUpdateDTO(
    val dateDelivery: LocalDate? = null,
    val collaboratorId: String? = null,
    val status: String? = null
)
