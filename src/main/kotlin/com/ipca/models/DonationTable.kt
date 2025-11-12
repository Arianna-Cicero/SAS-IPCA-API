package com.ipca.models

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.date


object DonationTable : Table("Donation"){
    val id = uuid("id_donation").autoGenerate()
    val nameDonor = varchar("name_donor", 100)
    val type = varchar("type", 100)
    val dateDonor = date("date_donor")
    val description = text("description").nullable()

    override val primaryKey = PrimaryKey(id, name = "PK_Donation_ID")
}