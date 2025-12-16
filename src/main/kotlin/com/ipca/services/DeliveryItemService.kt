package com.ipca.services

import com.ipca.models.BeneficiaryTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

data class DeliveryItemDTO(
    val idDelivery: UUID,
    val idGood: Int,
    val quantity: Int
)

object DeliveryItemService {

}