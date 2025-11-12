package com.ipca.models

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.javatime.date

object ExpirationAlertTable : Table("Expiration_Alert") {
    val id = integer("id_alert").autoIncrement()
    val idGood = integer("id_good")
                                        .references(
                                                GoodTable.id,
                                                onUpdate = ReferenceOption.CASCADE,
                                                onDelete = ReferenceOption.CASCADE
                                        )
    val dateAlert = date("date_alert")
    val remainingDays = integer("remaining_days")
    val resolved = bool("resolved").default(false)

    override val primaryKey = PrimaryKey(id, name = "PK_ExpirationAlert_ID")
}
