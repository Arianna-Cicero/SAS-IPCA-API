package com.ipca.dto.Course

import kotlinx.serialization.Serializable

@Serializable
data class CourseCreateDTO(
    val name: String
)
