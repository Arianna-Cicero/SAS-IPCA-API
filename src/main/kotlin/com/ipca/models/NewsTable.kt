package com.ipca.models

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.javatime.date


object NewsTable : Table("News") {
    val id = integer("id_news").autoIncrement()
    val title = varchar("title", 100)
    val content = text("content")
    val datePublication = date("date_publication")
    val idCampaign = integer("id_campaign")
            .references(
                    CampaignTable.id,
                    onUpdate = ReferenceOption.CASCADE,
                    onDelete = ReferenceOption.SET_NULL
            )
            .nullable()

    override val primaryKey = PrimaryKey(id, name = "PK_News_ID")
}
