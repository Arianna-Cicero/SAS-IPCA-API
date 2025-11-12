package com.ipca.models

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.javatime.date
import java.util.UUID

object DeliveryTable : Table("Delivery"){
    val id = uuid("id_delivery").autoGenerate()
    val idScheduling = integer("id_scheduling")
            .references(
                    SchedulingTable.id,
                    onUpdate = ReferenceOption.CASCADE,
                    onDelete = ReferenceOption.CASCADE
            )
    val dateDelivery = date("date_delivery")
    val status = varchar("status", 120)

    override val primaryKey = PrimaryKey(id, name = "PK_Delivery_ID")
}