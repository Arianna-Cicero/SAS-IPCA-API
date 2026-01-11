package com.ipca.dto.Dashboard

import kotlinx.serialization.Serializable

@Serializable
data class DashboardOverviewDTO(
    val active_beneficiaries: Int,
    val current_stock: Int,
    val pending_deliveries: Int,
    val next_pickups: Int
)
