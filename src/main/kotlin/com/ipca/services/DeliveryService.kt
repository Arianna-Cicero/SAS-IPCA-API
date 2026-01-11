package com.ipca.services

import com.ipca.dto.Delivery.DeliveryCreateDTO
import com.ipca.dto.Delivery.DeliveryResponseDTO
import com.ipca.dto.DeliveryItem.DeliveryItemResponseDTO
import com.ipca.models.DeliveryTable
import com.ipca.models.DeliveryItemTable
import com.ipca.models.GoodTable
import com.ipca.models.SchedulingTable
import com.ipca.models.BeneficiaryTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID
import com.ipca.utils.toKotlinx
import com.ipca.utils.toJava

object DeliveryService {

    fun getAll(): List<DeliveryResponseDTO> = transaction {
        (DeliveryTable innerJoin SchedulingTable innerJoin BeneficiaryTable)
            .selectAll()
            .map { row ->
                val deliveryId = row[DeliveryTable.id]
                val items = DeliveryItemTable
                    .select { DeliveryItemTable.idDelivery eq deliveryId }
                    .map { itemRow ->
                        val goodId = itemRow[DeliveryItemTable.idGood]
                        val good = GoodTable.select { GoodTable.id eq goodId }.singleOrNull()
                        DeliveryItemResponseDTO(
                            goodId = goodId,
                            goodName = good?.get(GoodTable.name) ?: "",
                            category = good?.get(GoodTable.category) ?: "",
                            quantity = itemRow[DeliveryItemTable.quantity]
                        )
                    }

                DeliveryResponseDTO(
                    id = deliveryId.toString(),
                    schedulingId = row[DeliveryTable.idScheduling],
                    beneficiaryName = row[BeneficiaryTable.name],
                    dateDelivery = row[DeliveryTable.dateDelivery].toKotlinx(),
                    status = row[DeliveryTable.status],
                    items = items
                )
            }
    }

    fun getById(id: String): DeliveryResponseDTO? = transaction {
        try {
            val uuid = UUID.fromString(id)
            (DeliveryTable innerJoin SchedulingTable innerJoin BeneficiaryTable)
                .select { DeliveryTable.id eq uuid }
                .singleOrNull()
                ?.let { row ->
                    DeliveryResponseDTO(
                        id = row[DeliveryTable.id].toString(),
                        schedulingId = row[DeliveryTable.idScheduling],
                        beneficiaryName = row[BeneficiaryTable.name],
                        dateDelivery = row[DeliveryTable.dateDelivery].toKotlinx(),
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
            it[dateDelivery] = request.dateDelivery.toJava()
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
