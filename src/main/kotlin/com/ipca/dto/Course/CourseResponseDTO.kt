package com.ipca.dto.Course

import kotlinx.serialization.Serializable

@Serializable
data class CourseResponseDTO(
    val id: Int,
    val name: String
)
