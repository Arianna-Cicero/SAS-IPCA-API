package com.ipca.services

import com.ipca.dto.Dashboard.DashboardOverviewDTO
import com.ipca.models.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDate

object DashboardService {

    fun getOverview(): DashboardOverviewDTO {
        return transaction {
            // Contar beneficiários ativos
            val activeBeneficiaries = BeneficiaryTable
                .selectAll()
                .count()
                .toInt()

            // Somar quantidade total de produtos disponíveis
            val currentStock = GoodTable
                .slice(GoodTable.quantity.sum())
                .selectAll()
                .mapNotNull { it[GoodTable.quantity.sum()] }
                .firstOrNull() ?: 0

            // Contar entregas pendentes (status não concluído)
            val pendingDeliveries = DeliveryTable
                .select { DeliveryTable.status neq "COMPLETED" }
                .count()
                .toInt()

            // Contar agendamentos futuros (data >= hoje)
            val today = LocalDate.now()
            val nextPickups = SchedulingTable
                .select { SchedulingTable.dateDelivery greaterEq today }
                .count()
                .toInt()

            DashboardOverviewDTO(
                active_beneficiaries = activeBeneficiaries,
                current_stock = currentStock,
                pending_deliveries = pendingDeliveries,
                next_pickups = nextPickups
            )
        }
    }
}
