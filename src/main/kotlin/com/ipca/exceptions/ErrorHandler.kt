package com.ipca.exceptions

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import kotlinx.serialization.Serializable
import org.slf4j.LoggerFactory

private val logger = LoggerFactory.getLogger("ErrorHandler")

@Serializable
data class ErrorResponse(
    val code: String,
    val message: String,
    val details: String? = null
)

fun Application.configureErrorHandling() {
    install(StatusPages) {
        // Handle validation exceptions
        exception<ValidationException> { call, cause ->
            logger.warn("Validation error: ${cause.message}")
            call.respond(
                HttpStatusCode.BadRequest,
                ErrorResponse(
                    code = "VALIDATION_ERROR",
                    message = cause.message ?: "Validation failed",
                    details = null
                )
            )
        }

        // Handle entity not found
        exception<EntityNotFoundException> { call, cause ->
            logger.warn("Entity not found: ${cause.entityType} with ${cause.identifier}")
            call.respond(
                HttpStatusCode.NotFound,
                ErrorResponse(
                    code = "NOT_FOUND",
                    message = cause.message ?: "Entity not found",
                    details = "${cause.entityType} with identifier ${cause.identifier}"
                )
            )
        }

        // Handle duplicate entity
        exception<EntityAlreadyExistsException> { call, cause ->
            logger.warn("Entity already exists: ${cause.entityType} ${cause.field}=${cause.identifier}")
            call.respond(
                HttpStatusCode.Conflict,
                ErrorResponse(
                    code = "ALREADY_EXISTS",
                    message = cause.message ?: "Entity already exists",
                    details = "${cause.entityType} with ${cause.field}=${cause.identifier}"
                )
            )
        }

        // Handle business rule violations
        exception<BusinessRuleViolationException> { call, cause ->
            logger.warn("Business rule violation: ${cause.message}")
            call.respond(
                HttpStatusCode.UnprocessableEntity,
                ErrorResponse(
                    code = "BUSINESS_RULE_VIOLATION",
                    message = cause.message ?: "Business rule violated",
                    details = null
                )
            )
        }

        // Handle authentication errors
        exception<AuthenticationException> { call, cause ->
            logger.warn("Authentication failed: ${cause.message}")
            call.respond(
                HttpStatusCode.Unauthorized,
                ErrorResponse(
                    code = "UNAUTHORIZED",
                    message = cause.message ?: "Authentication failed",
                    details = null
                )
            )
        }
        
        // Handle BadRequestException from invalid auth headers
        exception<io.ktor.server.plugins.BadRequestException> { call, cause ->
            if (cause.message?.contains("auth header", ignoreCase = true) == true ||
                cause.message?.contains("parseAuthorizationHeader", ignoreCase = true) == true) {
                val authHeaders = call.request.headers.getAll("Authorization")
                logger.warn("Invalid authorization header. Headers received: $authHeaders, Error: ${cause.message}")
                call.respond(
                    HttpStatusCode.Unauthorized,
                    ErrorResponse(
                        code = "INVALID_AUTH_HEADER",
                        message = "Invalid or malformed Authorization header",
                        details = "Please authenticate first using the 'Authorize' button with a valid token from /auth/login"
                    )
                )
            } else {
                logger.warn("Bad request: ${cause.message}")
                call.respond(
                    HttpStatusCode.BadRequest,
                    ErrorResponse(
                        code = "BAD_REQUEST",
                        message = cause.message ?: "Bad request",
                        details = null
                    )
                )
            }
        }

        // Handle authorization errors
        exception<AuthorizationException> { call, cause ->
            logger.warn("Authorization failed: ${cause.message}")
            call.respond(
                HttpStatusCode.Forbidden,
                ErrorResponse(
                    code = "FORBIDDEN",
                    message = cause.message ?: "Access denied",
                    details = null
                )
            )
        }

        // Handle all other exceptions
        exception<Exception> { call, cause ->
            logger.error("Unexpected error", cause)
            call.respond(
                HttpStatusCode.InternalServerError,
                ErrorResponse(
                    code = "INTERNAL_SERVER_ERROR",
                    message = "An unexpected error occurred",
                    details = if (System.getenv("ENVIRONMENT") == "development") cause.message else null
                )
            )
        }

        // Handle 404 - Not Found
        status(HttpStatusCode.NotFound) { call, status ->
            call.respond(
                status,
                ErrorResponse(
                    code = "NOT_FOUND",
                    message = "The requested resource was not found"
                )
            )
        }

        // Handle 405 - Method Not Allowed
        status(HttpStatusCode.MethodNotAllowed) { call, status ->
            call.respond(
                status,
                ErrorResponse(
                    code = "METHOD_NOT_ALLOWED",
                    message = "The HTTP method is not allowed for this resource"
                )
            )
        }
    }
}
