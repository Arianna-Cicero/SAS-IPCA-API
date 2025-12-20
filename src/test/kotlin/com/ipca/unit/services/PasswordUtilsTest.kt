package com.ipca.unit.services

import com.ipca.auth.PasswordUtils
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class PasswordUtilsTest {

    @Test
    fun `should hash password and produce different result than original`() {
        val password = "mySecurePassword123"
        val hashedPassword = PasswordUtils.hash(password)

        assertNotEquals(password, hashedPassword)
    }

    @Test
    fun `should verify correct password against hash`() {
        val password = "mySecurePassword123"
        val hashedPassword = PasswordUtils.hash(password)

        val isValid = PasswordUtils.verify(password, hashedPassword)

        assertTrue(isValid)
    }

    @Test
    fun `should reject incorrect password against hash`() {
        val password = "mySecurePassword123"
        val wrongPassword = "wrongPassword"
        val hashedPassword = PasswordUtils.hash(password)

        val isValid = PasswordUtils.verify(wrongPassword, hashedPassword)

        assertFalse(isValid)
    }

    @Test
    fun `should produce different hashes for same password`() {
        val password = "mySecurePassword123"
        val hash1 = PasswordUtils.hash(password)
        val hash2 = PasswordUtils.hash(password)

        assertNotEquals(hash1, hash2)
    }

    @Test
    fun `should handle empty password`() {
        val emptyPassword = ""
        val hashedPassword = PasswordUtils.hash(emptyPassword)

        assertTrue(PasswordUtils.verify(emptyPassword, hashedPassword))
    }
}
