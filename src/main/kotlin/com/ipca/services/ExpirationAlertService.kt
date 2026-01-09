package com.ipca.services

import com.ipca.dto.ExpirationAlert.ExpirationAlertCreateDTO
import com.ipca.dto.ExpirationAlert.ExpirationAlertResponseDTO
import com.ipca.models.ExpirationAlertTable
import com.ipca.models.GoodTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import com.ipca.utils.toKotlinx
import com.ipca.utils.todayKotlinx
import com.ipca.utils.toJava

object ExpirationAlertService {

    fun getAll(): List<ExpirationAlertResponseDTO> = transaction {
        ExpirationAlertTable.selectAll().map { row ->
            val goodId = row[ExpirationAlertTable.idGood]
            val good = GoodTable
                .select { GoodTable.id eq goodId }
                .singleOrNull()

            ExpirationAlertResponseDTO(
                id = row[ExpirationAlertTable.id],
                goodId = goodId,
                goodName = good?.get(GoodTable.name) ?: "",
                category = good?.get(GoodTable.category) ?: "",
                dateValidity = good?.get(GoodTable.dateValidity)?.toKotlinx() ?: todayKotlinx(),
                dateAlert = row[ExpirationAlertTable.dateAlert].toKotlinx(),
                remainingDays = row[ExpirationAlertTable.remainingDays],
                resolved = row[ExpirationAlertTable.resolved]
            )
        }
    }

    fun getById(id: Int): ExpirationAlertResponseDTO? = transaction {
        ExpirationAlertTable
            .select { ExpirationAlertTable.id eq id }
            .singleOrNull()
            ?.let { row ->
                val goodId = row[ExpirationAlertTable.idGood]
                val good = GoodTable
                    .select { GoodTable.id eq goodId }
                    .singleOrNull()

                ExpirationAlertResponseDTO(
                    id = row[ExpirationAlertTable.id],
                    goodId = goodId,
                    goodName = good?.get(GoodTable.name) ?: "",
                    category = good?.get(GoodTable.category) ?: "",
                    dateValidity = good?.get(GoodTable.dateValidity)?.toKotlinx() ?: todayKotlinx(),
                    dateAlert = row[ExpirationAlertTable.dateAlert].toKotlinx(),
                    remainingDays = row[ExpirationAlertTable.remainingDays],
                    resolved = row[ExpirationAlertTable.resolved]
                )
            }
    }

    fun create(request: ExpirationAlertCreateDTO): Int = transaction {
        val insert = ExpirationAlertTable.insert { row ->
            row[idGood] = request.goodId
            row[dateAlert] = request.dateAlert.toJava()
            row[remainingDays] = request.remainingDays
            row[resolved] = false
        }
        insert.resultedValues?.first()?.get(ExpirationAlertTable.id)
            ?: error("Failed to insert ExpirationAlert")
    }

    fun resolve(id: Int, resolved: Boolean) = transaction {
        ExpirationAlertTable.update({ ExpirationAlertTable.id eq id }) { row ->
            row[ExpirationAlertTable.resolved] = resolved
        }
    }

    fun delete(id: Int) = transaction {
        ExpirationAlertTable.deleteWhere { ExpirationAlertTable.id eq id }
    }

    fun getUnresolved(): List<ExpirationAlertResponseDTO> = transaction {
        ExpirationAlertTable
            .select { ExpirationAlertTable.resolved eq false }
            .map { row ->
                val goodId = row[ExpirationAlertTable.idGood]
                val good = GoodTable
                    .select { GoodTable.id eq goodId }
                    .singleOrNull()

                ExpirationAlertResponseDTO(
                    id = row[ExpirationAlertTable.id],
                    goodId = goodId,
                    goodName = good?.get(GoodTable.name) ?: "",
                    category = good?.get(GoodTable.category) ?: "",
                    dateValidity = good?.get(GoodTable.dateValidity)?.toKotlinx() ?: todayKotlinx(),
                    dateAlert = row[ExpirationAlertTable.dateAlert].toKotlinx(),
                    remainingDays = row[ExpirationAlertTable.remainingDays],
                    resolved = row[ExpirationAlertTable.resolved]
                )
            }
    }
}
