package com.ipca.services

import com.ipca.dto.Good.GoodCreateDTO
import com.ipca.dto.Good.GoodResponseDTO
import com.ipca.dto.Good.GoodUpdateDTO
import com.ipca.dto.Donation.DonationResponseDTO
import com.ipca.models.GoodTable
import com.ipca.models.DonationTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object GoodService {

    fun getAll(): List<GoodResponseDTO> = transaction {
        GoodTable.selectAll().map { row ->
            val donationId = row[GoodTable.idDonation]
            val donation = DonationTable
                .select { DonationTable.id eq donationId }
                .singleOrNull()
                ?.let { donRow ->
                    DonationResponseDTO(
                        id = donRow[DonationTable.id].toString(),
                        nameDonor = donRow[DonationTable.nameDonor],
                        type = donRow[DonationTable.type],
                        dateDonor = donRow[DonationTable.dateDonor],
                        description = donRow[DonationTable.description]
                    )
                }

            GoodResponseDTO(
                id = row[GoodTable.id],
                name = row[GoodTable.name],
                category = row[GoodTable.category],
                quantity = row[GoodTable.quantity],
                intake = row[GoodTable.intake],
                dateValidity = row[GoodTable.dateValidity],
                status = row[GoodTable.status],
                donation = donation ?: DonationResponseDTO("", "", "", null, null)
            )
        }
    }

    fun getById(id: Int): GoodResponseDTO? = transaction {
        GoodTable
            .select { GoodTable.id eq id }
            .singleOrNull()
            ?.let { row ->
                val donationId = row[GoodTable.idDonation]
                val donation = DonationTable
                    .select { DonationTable.id eq donationId }
                    .singleOrNull()
                    ?.let { donRow ->
                        DonationResponseDTO(
                            id = donRow[DonationTable.id].toString(),
                            nameDonor = donRow[DonationTable.nameDonor],
                            type = donRow[DonationTable.type],
                            dateDonor = donRow[DonationTable.dateDonor],
                            description = donRow[DonationTable.description]
                        )
                    }

                GoodResponseDTO(
                    id = row[GoodTable.id],
                    name = row[GoodTable.name],
                    category = row[GoodTable.category],
                    quantity = row[GoodTable.quantity],
                    intake = row[GoodTable.intake],
                    dateValidity = row[GoodTable.dateValidity],
                    status = row[GoodTable.status],
                    donation = donation ?: DonationResponseDTO("", "", "", null, null)
                )
            }
    }

    fun create(request: GoodCreateDTO): Int = transaction {
        GoodTable.insertAndGetId {
            it[name] = request.name
            it[category] = request.category
            it[quantity] = request.quantity
            it[intake] = request.intake
            it[dateValidity] = request.dateValidity
            it[status] = "available"
            it[idDonation] = try {
                java.util.UUID.fromString(request.donationId)
            } catch (e: Exception) {
                java.util.UUID.randomUUID()
            }
        }.value
    }

    fun update(id: Int, request: GoodUpdateDTO) = transaction {
        GoodTable.update({ GoodTable.id eq id }) { row ->
            request.name?.let { row[GoodTable.name] = it }
            request.category?.let { row[GoodTable.category] = it }
            request.quantity?.let { row[GoodTable.quantity] = it }
            request.dateValidity?.let { row[GoodTable.dateValidity] = it }
            request.status?.let { row[GoodTable.status] = it }
        }
    }

    fun delete(id: Int) = transaction {
        GoodTable.deleteWhere { GoodTable.id eq id }
    }

    fun getByCategory(category: String): List<GoodResponseDTO> = transaction {
        GoodTable
            .select { GoodTable.category eq category }
            .map { row ->
                val donationId = row[GoodTable.idDonation]
                val donation = DonationTable
                    .select { DonationTable.id eq donationId }
                    .singleOrNull()
                    ?.let { donRow ->
                        DonationResponseDTO(
                            id = donRow[DonationTable.id].toString(),
                            nameDonor = donRow[DonationTable.nameDonor],
                            type = donRow[DonationTable.type],
                            dateDonor = donRow[DonationTable.dateDonor],
                            description = donRow[DonationTable.description]
                        )
                    }

                GoodResponseDTO(
                    id = row[GoodTable.id],
                    name = row[GoodTable.name],
                    category = row[GoodTable.category],
                    quantity = row[GoodTable.quantity],
                    intake = row[GoodTable.intake],
                    dateValidity = row[GoodTable.dateValidity],
                    status = row[GoodTable.status],
                    donation = donation ?: DonationResponseDTO("", "", "", null, null)
                )
            }
    }
}
