package com.ipca.unit.logic

import kotlinx.datetime.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import com.ipca.utils.toJava

class GoodExpirationLogicTest {

    @Test
    fun `should identify expired goods`() {
        val validityDate = LocalDate(2024, 12, 31)
        val isExpired = checkIfExpired(validityDate)

        assertTrue(isExpired)
    }

    @Test
    fun `should identify non-expired goods`() {
        val validityDate = LocalDate(2026, 1, 11)
        val isExpired = checkIfExpired(validityDate)

        assertFalse(isExpired)
    }

    @Test
    fun `should identify goods expiring today as expired`() {
        val validityDate = LocalDate(2025, 1, 1)
        val isExpired = checkIfExpired(validityDate)

        assertTrue(isExpired)
    }

    @Test
    fun `should calculate remaining days correctly`() {
        val today = LocalDate(2025, 1, 1)
        val validityDate = LocalDate(2025, 1, 6)

        val remainingDays = calculateRemainingDays(today, validityDate)

        assertEquals(5, remainingDays)
    }

    @Test
    fun `should calculate remaining days as negative for expired goods`() {
        // use earlier date to produce negative
        val remainingDays = calculateRemainingDays(LocalDate(2025, 1, 1), LocalDate(2024, 12, 22))

        assertEquals(-10, remainingDays)
    }

    @Test
    fun `should determine alert status based on remaining days`() {
        // Critical: less than 3 days
        assertTrue(shouldCreateAlert(2))

        // Warning: less than 7 days
        assertTrue(shouldCreateAlert(5))

        // No alert: more than 7 days
        assertFalse(shouldCreateAlert(10))
    }

    @Test
    fun `should handle quantity validation`() {
        assertTrue(isValidQuantity(0))    // Zero is valid (empty)
        assertTrue(isValidQuantity(100))  // Positive is valid
        assertFalse(isValidQuantity(-1))  // Negative is invalid
    }

    // Helper functions for business logic
    private fun checkIfExpired(validityDate: LocalDate): Boolean {
        val today = LocalDate(2025, 1, 1)
        return validityDate < today || validityDate == today
    }

    private fun calculateRemainingDays(today: LocalDate, validityDate: LocalDate): Long {
        return java.time.temporal.ChronoUnit.DAYS.between(today.toJava(), validityDate.toJava())
    }

    private fun shouldCreateAlert(remainingDays: Long): Boolean {
        return remainingDays < 7
    }

    private fun isValidQuantity(quantity: Int): Boolean {
        return quantity >= 0
    }
}
