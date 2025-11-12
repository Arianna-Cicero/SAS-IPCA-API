package com.ipca.models

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.date

object CampaignTable : Table("Campaign") {
    val id = integer("id_campaign").autoIncrement()
    val title = varchar("title", 100)
    val description = text("description")
    val dateStart = date("date_start")
    val dateEnd = date("date_end")
    val totalCollected = integer("total_collected").default(0)

    override val primaryKey = PrimaryKey(id, name = "PK_Campaign_ID")
}
