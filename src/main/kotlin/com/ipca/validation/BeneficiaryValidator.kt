package com.ipca.validation

import com.ipca.models.CourseTable
import com.ipca.models.BeneficiaryTable
import com.ipca.dto.Beneficiary.BeneficiaryCreateDTO
import com.ipca.dto.Beneficiary.BeneficiaryUpdateDTO
import com.ipca.exceptions.ValidationException
import com.ipca.exceptions.EntityNotFoundException
import com.ipca.exceptions.EntityAlreadyExistsException
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

object BeneficiaryValidator {
    
    private val EMAIL_PATTERN = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
    private val PHONE_PATTERN = Regex("^[0-9]{9}$")
    
    fun validateCreate(dto: BeneficiaryCreateDTO) {
        validateName(dto.name)
        validateEmail(dto.email)
        validatePhone(dto.telephone)
        validateStudentNumber(dto.studentNumber, isUpdate = false)
        validateCourseExists(dto.idCourse)
    }
    
    fun validateUpdate(dto: BeneficiaryUpdateDTO, existingStudentNumber: Int? = null) {
        if (dto.name != null) validateName(dto.name)
        if (dto.email != null) validateEmail(dto.email)
        if (dto.telephone != null) validatePhone(dto.telephone)
        if (dto.studentNumber != null) validateStudentNumber(dto.studentNumber, isUpdate = true, existingNumber = existingStudentNumber)
        if (dto.idCourse != null) validateCourseExists(dto.idCourse)
    }
    
    private fun validateName(name: String) {
        if (name.isBlank()) {
            throw ValidationException("Beneficiary name cannot be empty", "beneficiary")
        }
        if (name.length < 3 || name.length > 255) {
            throw ValidationException("Beneficiary name must be between 3 and 255 characters", "beneficiary")
        }
    }
    
    private fun validateEmail(email: String) {
        if (!EMAIL_PATTERN.matches(email)) {
            throw ValidationException("Invalid email format: $email", "beneficiary")
        }
    }
    
    private fun validatePhone(phone: String) {
        if (!PHONE_PATTERN.matches(phone)) {
            throw ValidationException("Phone number must be exactly 9 digits", "beneficiary")
        }
    }
    
    private fun validateStudentNumber(studentNumber: Int, isUpdate: Boolean = false, existingNumber: Int? = null) {
        // If updating with the same student number, skip uniqueness check
        if (isUpdate && studentNumber == existingNumber) {
            return
        }
        
        // Check uniqueness
        val exists = transaction {
            BeneficiaryTable.select { BeneficiaryTable.studentNumber eq studentNumber }.count() > 0
        }
        
        if (exists) {
            throw EntityAlreadyExistsException("beneficiary", "studentNumber", studentNumber.toString())
        }
    }
    
    private fun validateCourseExists(courseId: Int) {
        val exists = transaction {
            CourseTable.select { CourseTable.id eq courseId }.count() > 0
        }
        
        if (!exists) {
            throw EntityNotFoundException("course", courseId.toString())
        }
    }
}
