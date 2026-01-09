package com.ipca.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import com.ipca.exceptions.AuthenticationException
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*

data class JwtConfig(
    val secret: String = System.getenv("JWT_SECRET") ?: "your-secret-key-change-in-production",
    val issuer: String = System.getenv("JWT_ISSUER") ?: "loja-social-ipca",
    val audience: String = System.getenv("JWT_AUDIENCE") ?: "loja-social-api",
    val expirationHours: Long = System.getenv("JWT_EXPIRATION_HOURS")?.toLong() ?: 8L
)

object JwtProvider {
    private lateinit var config: JwtConfig
    private lateinit var algorithm: Algorithm

    fun init(jwtConfig: JwtConfig = JwtConfig()) {
        config = jwtConfig
        algorithm = Algorithm.HMAC256(config.secret)
    }

    fun generateToken(collaboratorId: UUID, email: String): String {
        val now = Instant.now()
        val expiresAt = now.plus(config.expirationHours, ChronoUnit.HOURS)

        return JWT.create()
            .withIssuer(config.issuer)
            .withAudience(config.audience)
            .withIssuedAt(Date.from(now))
            .withExpiresAt(Date.from(expiresAt))
            .withSubject(collaboratorId.toString())
            .withClaim("email", email)
            .sign(algorithm)
    }

    fun verifyToken(token: String): TokenPayload {
        return try {
            val decodedToken = JWT.require(algorithm)
                .withIssuer(config.issuer)
                .withAudience(config.audience)
                .build()
                .verify(token)

            val collaboratorId = UUID.fromString(decodedToken.subject)
            val email = decodedToken.getClaim("email").asString()

            TokenPayload(collaboratorId, email)
        } catch (e: JWTVerificationException) {
            throw AuthenticationException("Invalid or expired token: ${e.message}", e)
        } catch (e: Exception) {
            throw AuthenticationException("Token validation failed: ${e.message}", e)
        }
    }

    fun isTokenValid(token: String): Boolean {
        return try {
            verifyToken(token)
            true
        } catch (e: Exception) {
            false
        }
    }
}

data class TokenPayload(
    val collaboratorId: UUID,
    val email: String
)
