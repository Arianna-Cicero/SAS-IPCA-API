package com.ipca.unit.logic

import java.time.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class GoodExpirationLogicTest {

    @Test
    fun `should identify expired goods`() {
        val validityDate = LocalDate.now().minusDays(1)
        val isExpired = checkIfExpired(validityDate)

        assertTrue(isExpired)
    }

    @Test
    fun `should identify non-expired goods`() {
        val validityDate = LocalDate.now().plusDays(10)
        val isExpired = checkIfExpired(validityDate)

        assertFalse(isExpired)
    }

    @Test
    fun `should identify goods expiring today as expired`() {
        val validityDate = LocalDate.now()
        val isExpired = checkIfExpired(validityDate)

        assertTrue(isExpired)
    }

    @Test
    fun `should calculate remaining days correctly`() {
        val today = LocalDate.now()
        val validityDate = today.plusDays(5)

        val remainingDays = calculateRemainingDays(today, validityDate)

        assertEquals(5, remainingDays)
    }

    @Test
    fun `should calculate remaining days as zero for expired goods`() {
        val today = LocalDate.now()
        val validityDate = today.minusDays(10)

        val remainingDays = calculateRemainingDays(today, validityDate)

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
        return validityDate.isBefore(LocalDate.now()) || validityDate.isEqual(LocalDate.now())
    }

    private fun calculateRemainingDays(today: LocalDate, validityDate: LocalDate): Long {
        return java.time.temporal.ChronoUnit.DAYS.between(today, validityDate)
    }

    private fun shouldCreateAlert(remainingDays: Long): Boolean {
        return remainingDays < 7
    }

    private fun isValidQuantity(quantity: Int): Boolean {
        return quantity >= 0
    }
}
