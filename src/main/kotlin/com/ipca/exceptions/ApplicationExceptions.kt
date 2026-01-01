package com.ipca.exceptions

/**
 * Base exception for all application exceptions
 */
sealed class ApplicationException(message: String, cause: Throwable? = null) : Exception(message, cause)

/**
 * Thrown when validation fails
 */
class ValidationException(message: String, cause: Throwable? = null) : ApplicationException(message, cause) {
    // Convenience overload used by validators passing an entity type
    constructor(message: String, entityType: String) : this("$message [entity=$entityType]", null)
}

/**
 * Thrown when an entity is not found
 */
class EntityNotFoundException(
    message: String,
    val entityType: String,
    val identifier: Any
) : ApplicationException(message) {
    // Convenience overload used by validators without custom message
    constructor(entityType: String, identifier: Any) : this(
        message = "$entityType not found for identifier $identifier",
        entityType = entityType,
        identifier = identifier
    )
}

/**
 * Thrown when entity already exists (duplicate)
 */
class EntityAlreadyExistsException(
    val entityType: String,
    val field: String,
    val identifier: Any
) : ApplicationException("$entityType already exists with $field=$identifier")

/**
 * Thrown when an operation violates business rules
 */
class BusinessRuleViolationException(message: String, cause: Throwable? = null) : ApplicationException(message, cause)

/**
 * Thrown when a foreign key constraint is violated
 */
class ForeignKeyConstraintException(
    message: String,
    val sourceEntity: String,
    val targetEntity: String,
    val foreignKey: String
) : ApplicationException(message) {
    // Convenience overload when a default message is acceptable
    constructor(sourceEntity: String, targetEntity: String, foreignKey: String) : this(
        message = "Foreign key constraint: $sourceEntity -> $targetEntity on $foreignKey",
        sourceEntity = sourceEntity,
        targetEntity = targetEntity,
        foreignKey = foreignKey
    )
}

/**
 * Thrown when trying an invalid status transition
 */
class InvalidStatusTransitionException(
    message: String,
    val currentStatus: String,
    val targetStatus: String
) : ApplicationException(message) {
    // Convenience overload used by validators with entity and field context
    constructor(entityType: String, field: String, currentStatus: String, targetStatus: String) : this(
        message = "Invalid status transition for $entityType $field: $currentStatus -> $targetStatus",
        currentStatus = currentStatus,
        targetStatus = targetStatus
    )
}

/**
 * Thrown when a session is invalid or expired
 */
class InvalidSessionException(message: String, cause: Throwable? = null) : ApplicationException(message, cause)
