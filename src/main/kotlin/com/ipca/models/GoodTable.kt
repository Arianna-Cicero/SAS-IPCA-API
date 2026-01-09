package com.ipca.models

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.javatime.date

object GoodTable : Table("Good") {
    val id = integer("id_good").autoIncrement()
    val name = varchar("name", 100)
    val category = varchar("category", 100)
    val quantity = integer("quantity").check { it greaterEq 0 }
    val intake = date("intake_date")
    val dateValidity = date("date_validity")
    val status = varchar("status", 120)
    val idDonation = uuid("id_donation").references(
                                                        DonationTable.id,
                                                        onUpdate = ReferenceOption.CASCADE,
                                                        onDelete = ReferenceOption.SET_NULL
                                                        )

    override val primaryKey = PrimaryKey(id, name = "PK_Good_ID")
}
