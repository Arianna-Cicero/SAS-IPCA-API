package com.ipca.unit.validators

import java.time.LocalDate
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ValidationUtilsTest {

    @Test
    fun `should validate correct email format`() {
        val validEmails = listOf(
            "user@example.com",
            "test.email@ipca.pt",
            "first.last+tag@company.co.uk"
        )

        validEmails.forEach { email ->
            assertTrue(isValidEmail(email), "Email should be valid: $email")
        }
    }

    @Test
    fun `should reject invalid email format`() {
        val invalidEmails = listOf(
            "plainaddress",
            "@missinglocal.com",
            "missing@domain",
            "spaces in@email.com",
            ""
        )

        invalidEmails.forEach { email ->
            assertFalse(isValidEmail(email), "Email should be invalid: $email")
        }
    }

    @Test
    fun `should validate phone number format`() {
        val validPhones = listOf(
            "912345678",
            "917654321",
            "968123456"
        )

        validPhones.forEach { phone ->
            assertTrue(isValidPhoneNumber(phone), "Phone should be valid: $phone")
        }
    }

    @Test
    fun `should reject invalid phone numbers`() {
        val invalidPhones = listOf(
            "123",           // Too short
            "12345678901",   // Too long
            "abcdefghij",    // Non-numeric
            "+351912345678", // Has prefix
            ""               // Empty
        )

        invalidPhones.forEach { phone ->
            assertFalse(isValidPhoneNumber(phone), "Phone should be invalid: $phone")
        }
    }

    @Test
    fun `should validate date ranges`() {
        val startDate = LocalDate.of(2025, 12, 1)
        val endDate = LocalDate.of(2025, 12, 31)

        assertTrue(isValidDateRange(startDate, endDate))
    }

    @Test
    fun `should reject invalid date ranges`() {
        val startDate = LocalDate.of(2025, 12, 31)
        val endDate = LocalDate.of(2025, 12, 1)

        assertFalse(isValidDateRange(startDate, endDate))
    }

    @Test
    fun `should accept equal start and end dates`() {
        val date = LocalDate.of(2025, 12, 20)

        assertTrue(isValidDateRange(date, date))
    }

    // Helper validation functions
    private fun isValidEmail(email: String): Boolean {
        val emailRegex = Regex("^[A-Za-z0-9+_.-]+@(.+)$")
        return emailRegex.matches(email) && email.contains("@") && email.contains(".")
    }

    private fun isValidPhoneNumber(phone: String): Boolean {
        return phone.matches(Regex("^[0-9]{9}$"))
    }

    private fun isValidDateRange(startDate: LocalDate, endDate: LocalDate): Boolean {
        return !startDate.isAfter(endDate)
    }
}
