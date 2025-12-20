package com.ipca.services

import com.ipca.dto.Delivery.DeliveryCreateDTO
import com.ipca.dto.Delivery.DeliveryResponseDTO
import com.ipca.models.DeliveryTable
import com.ipca.models.DeliveryItemTable
import com.ipca.models.GoodTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

object DeliveryService {

    fun getAll(): List<DeliveryResponseDTO> = transaction {
        DeliveryTable.selectAll().map { row ->
            val deliveryId = row[DeliveryTable.id]
            val items = DeliveryItemTable
                .select { DeliveryItemTable.idDelivery eq deliveryId }
                .map { itemRow ->
                    mapOf(
                        "goodId" to itemRow[DeliveryItemTable.idGood],
                        "goodName" to GoodTable.select { GoodTable.id eq itemRow[DeliveryItemTable.idGood] }.singleOrNull()?.get(GoodTable.name),
                        "category" to GoodTable.select { GoodTable.id eq itemRow[DeliveryItemTable.idGood] }.singleOrNull()?.get(GoodTable.category),
                        "quantity" to itemRow[DeliveryItemTable.quantity]
                    )
                }

            DeliveryResponseDTO(
                id = deliveryId.toString(),
                schedulingId = row[DeliveryTable.idScheduling],
                dateDelivery = row[DeliveryTable.dateDelivery],
                status = row[DeliveryTable.status],
                items = emptyList()
            )
        }
    }

    fun getById(id: String): DeliveryResponseDTO? = transaction {
        try {
            val uuid = UUID.fromString(id)
            DeliveryTable
                .select { DeliveryTable.id eq uuid }
                .singleOrNull()
                ?.let { row ->
                    DeliveryResponseDTO(
                        id = row[DeliveryTable.id].toString(),
                        schedulingId = row[DeliveryTable.idScheduling],
                        dateDelivery = row[DeliveryTable.dateDelivery],
                        status = row[DeliveryTable.status],
                        items = emptyList()
                    )
                }
        } catch (e: Exception) {
            null
        }
    }

    fun create(request: DeliveryCreateDTO): String = transaction {
        val id = UUID.randomUUID()
        DeliveryTable.insert {
            it[DeliveryTable.id] = id
            it[idScheduling] = request.schedulingId
            it[dateDelivery] = request.dateDelivery
            it[status] = "pending"
        }
        id.toString()
    }

    fun updateStatus(id: String, status: String) = transaction {
        try {
            val uuid = UUID.fromString(id)
            DeliveryTable.update({ DeliveryTable.id eq uuid }) { row ->
                row[DeliveryTable.status] = status
            }
        } catch (e: Exception) {
            // Invalid UUID format
        }
    }

    fun delete(id: String) = transaction {
        try {
            val uuid = UUID.fromString(id)
            DeliveryTable.deleteWhere { DeliveryTable.id eq uuid }
        } catch (e: Exception) {
            // Invalid UUID format
        }
    }
}
