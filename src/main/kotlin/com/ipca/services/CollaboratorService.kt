package com.ipca.services

import com.ipca.models.BeneficiaryTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

data class CollaboratorDTO(
    val id: UUID,
    val name: String,
    val email: String,
    val password: String,
    val profile: String,
    val active: Boolean
)

object CollaboratorService {

}