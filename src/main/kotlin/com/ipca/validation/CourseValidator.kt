package com.ipca.validation

import com.ipca.dto.Course.CourseCreateDTO
import com.ipca.dto.Course.CourseUpdateDTO
import com.ipca.exceptions.ValidationException

object CourseValidator {
    
    fun validateCreate(dto: CourseCreateDTO) {
        validateName(dto.name)
    }
    
    fun validateUpdate(dto: CourseUpdateDTO) {
        if (dto.name != null) validateName(dto.name)
    }
    
    private fun validateName(name: String) {
        if (name.isBlank()) {
            throw ValidationException("Course name cannot be empty", "course")
        }
        if (name.length < 3 || name.length > 255) {
            throw ValidationException("Course name must be between 3 and 255 characters", "course")
        }
    }
}
