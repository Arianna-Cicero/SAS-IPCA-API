package com.ipca.utils

import com.ipca.auth.PasswordUtils
import org.junit.jupiter.api.Test

class PasswordHashGenerator {
    
    @Test
    fun generateHash() {
        val password = "admin123"
        val hash = PasswordUtils.hash(password)
        
        println("========================================")
        println("Password: $password")
        println("Hashed: $hash")
        println("========================================")
        println("\nCopy the hash above and use it in your database!")
    }
}
