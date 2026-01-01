package com.ipca.services

import com.ipca.dto.Donation.DonationCreateDTO
import com.ipca.dto.Donation.DonationResponseDTO
import com.ipca.dto.Donation.DonationUpdateDTO
import com.ipca.models.DonationTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

object DonationService {

    fun getAll(): List<DonationResponseDTO> = transaction {
        DonationTable.selectAll().map { row ->
            DonationResponseDTO(
                id = row[DonationTable.id].toString(),
                nameDonor = row[DonationTable.nameDonor],
                type = row[DonationTable.type],
                dateDonor = row[DonationTable.dateDonor],
                description = row[DonationTable.description]
            )
        }
    }

    fun getById(id: String): DonationResponseDTO? = transaction {
        try {
            val uuid = UUID.fromString(id)
            DonationTable
                .select { DonationTable.id eq uuid }
                .singleOrNull()
                ?.let { row ->
                    DonationResponseDTO(
                        id = row[DonationTable.id].toString(),
                        nameDonor = row[DonationTable.nameDonor],
                        type = row[DonationTable.type],
                        dateDonor = row[DonationTable.dateDonor],
                        description = row[DonationTable.description]
                    )
                }
        } catch (e: Exception) {
            null
        }
    }

    fun create(request: DonationCreateDTO): String = transaction {
        val id = UUID.randomUUID()
        DonationTable.insert {
            it[DonationTable.id] = id
            it[nameDonor] = request.nameDonor
            it[type] = request.type
            it[dateDonor] = request.dateDonor
            it[description] = request.description
        }
        id.toString()
    }

    fun update(id: String, request: DonationUpdateDTO) = transaction {
        try {
            val uuid = UUID.fromString(id)
            DonationTable.update({ DonationTable.id eq uuid }) { row ->
                request.nameDonor?.let { row[DonationTable.nameDonor] = it }
                request.type?.let { row[DonationTable.type] = it }
                request.dateDonor?.let { row[DonationTable.dateDonor] = it }
                request.description?.let { row[DonationTable.description] = it }
            }
        } catch (e: Exception) {
            // Invalid UUID format
        }
    }

    fun delete(id: String) = transaction {
        try {
            val uuid = UUID.fromString(id)
            DonationTable.deleteWhere { DonationTable.id eq uuid }
        } catch (e: Exception) {
            // Invalid UUID format
        }
    }
}
