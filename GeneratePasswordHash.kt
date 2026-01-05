import at.favre.lib.crypto.bcrypt.BCrypt

fun main() {
    // Change this to whatever password you want to hash
    val password = "admin123"
    
    val hashedPassword = BCrypt.withDefaults().hashToString(12, password.toCharArray())
    
    println("Password: $password")
    println("Hashed: $hashedPassword")
    println("\nUse this hashed value in your database!")
}
