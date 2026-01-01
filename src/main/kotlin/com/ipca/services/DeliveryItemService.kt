package com.ipca.services

import com.ipca.dto.DeliveryItem.DeliveryItemCreateDTO
import com.ipca.dto.DeliveryItem.DeliveryItemResponseDTO
import com.ipca.models.DeliveryItemTable
import com.ipca.models.GoodTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

object DeliveryItemService {

    fun getAll(): List<DeliveryItemResponseDTO> = transaction {
        DeliveryItemTable.selectAll().map { row ->
            val goodId = row[DeliveryItemTable.idGood]
            val good = GoodTable
                .select { GoodTable.id eq goodId }
                .singleOrNull()

            DeliveryItemResponseDTO(
                goodId = goodId,
                goodName = good?.get(GoodTable.name) ?: "",
                category = good?.get(GoodTable.category) ?: "",
                quantity = row[DeliveryItemTable.quantity]
            )
        }
    }

    fun getByDeliveryId(deliveryId: String): List<DeliveryItemResponseDTO> = transaction {
        try {
            val uuid = UUID.fromString(deliveryId)
            DeliveryItemTable
                .select { DeliveryItemTable.idDelivery eq uuid }
                .map { row ->
                    val goodId = row[DeliveryItemTable.idGood]
                    val good = GoodTable
                        .select { GoodTable.id eq goodId }
                        .singleOrNull()

                    DeliveryItemResponseDTO(
                        goodId = goodId,
                        goodName = good?.get(GoodTable.name) ?: "",
                        category = good?.get(GoodTable.category) ?: "",
                        quantity = row[DeliveryItemTable.quantity]
                    )
                }
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun create(request: DeliveryItemCreateDTO) = transaction {
        try {
            // Assuming deliveryId comes from the request, we need to parse it
            DeliveryItemTable.insert {
                it[idDelivery] = UUID.randomUUID() // This should come from request context
                it[idGood] = request.goodId
                it[quantity] = request.quantity
            }
        } catch (e: Exception) {
            // Handle error
        }
    }

    fun delete(deliveryId: String, goodId: Int) = transaction {
        try {
            val uuid = UUID.fromString(deliveryId)
            DeliveryItemTable.deleteWhere { (DeliveryItemTable.idDelivery eq uuid) and (DeliveryItemTable.idGood eq goodId) }
        } catch (e: Exception) {
            // Invalid UUID format
        }
    }
}