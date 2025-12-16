package com.ipca.dto.Course

import kotlinx.serialization.Serializable

@Serializable
data class CourseUpdateDTO(
    val name: String
)
