package com.ipca.models

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.ReferenceOption

object DeliveryItemTable : Table("Delivery_Item") {
    val idDelivery = uuid("id_delivery")
                                                .references(
                                                        DeliveryTable.id,
                                                        onUpdate = ReferenceOption.CASCADE,
                                                        onDelete = ReferenceOption.CASCADE
                                                )
    val idGood = integer("id_good")
                                        .references(
                                                GoodTable.id,
                                                onUpdate = ReferenceOption.CASCADE,
                                                onDelete = ReferenceOption.CASCADE
                                        )
    val quantity = integer("quantity").check { it greaterEq 0 }

    override val primaryKey = PrimaryKey(idDelivery, idGood, name = "PK_DeliveryItem")
}
