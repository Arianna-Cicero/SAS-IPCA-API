package com.ipca.services

import com.ipca.dto.News.NewsCreateDTO
import com.ipca.dto.News.NewsResponseDTO
import com.ipca.dto.News.NewsUpdateDTO
import com.ipca.dto.Campaign.CampaignSummaryDTO
import com.ipca.models.NewsTable
import com.ipca.models.CampaignTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object NewsService {

    fun getAll(): List<NewsResponseDTO> = transaction {
        NewsTable.selectAll().map { row ->
            val campaignId = row[NewsTable.idCampaign]
            val campaign = if (campaignId != null) {
                CampaignTable
                    .select { CampaignTable.id eq campaignId }
                    .singleOrNull()
                    ?.let { camRow ->
                        CampaignSummaryDTO(
                            id = camRow[CampaignTable.id],
                            title = camRow[CampaignTable.title]
                        )
                    }
            } else {
                null
            }

            NewsResponseDTO(
                id = row[NewsTable.id],
                title = row[NewsTable.title],
                content = row[NewsTable.content],
                datePublication = row[NewsTable.datePublication],
                campaign = campaign
            )
        }
    }

    fun getById(id: Int): NewsResponseDTO? = transaction {
        NewsTable
            .select { NewsTable.id eq id }
            .singleOrNull()
            ?.let { row ->
                val campaignId = row[NewsTable.idCampaign]
                val campaign = if (campaignId != null) {
                    CampaignTable
                        .select { CampaignTable.id eq campaignId }
                        .singleOrNull()
                        ?.let { camRow ->
                            CampaignSummaryDTO(
                                id = camRow[CampaignTable.id],
                                title = camRow[CampaignTable.title]
                            )
                        }
                } else {
                    null
                }

                NewsResponseDTO(
                    id = row[NewsTable.id],
                    title = row[NewsTable.title],
                    content = row[NewsTable.content],
                    datePublication = row[NewsTable.datePublication],
                    campaign = campaign
                )
            }
    }

    fun create(request: NewsCreateDTO): Int = transaction {
        NewsTable.insertAndGetId {
            it[title] = request.title
            it[content] = request.content
            it[datePublication] = request.datePublication
            it[idCampaign] = request.campaignId
        }.value
    }

    fun update(id: Int, request: NewsUpdateDTO) = transaction {
        NewsTable.update({ NewsTable.id eq id }) { row ->
            request.title?.let { row[NewsTable.title] = it }
            request.content?.let { row[NewsTable.content] = it }
            request.datePublication?.let { row[NewsTable.datePublication] = it }
            request.campaignId?.let { row[NewsTable.idCampaign] = it }
        }
    }

    fun delete(id: Int) = transaction {
        NewsTable.deleteWhere { NewsTable.id eq id }
    }

    fun getByCampaignId(campaignId: Int): List<NewsResponseDTO> = transaction {
        NewsTable
            .select { NewsTable.idCampaign eq campaignId }
            .map { row ->
                val campaign = CampaignTable
                    .select { CampaignTable.id eq campaignId }
                    .singleOrNull()
                    ?.let { camRow ->
                        CampaignSummaryDTO(
                            id = camRow[CampaignTable.id],
                            title = camRow[CampaignTable.title]
                        )
                    }

                NewsResponseDTO(
                    id = row[NewsTable.id],
                    title = row[NewsTable.title],
                    content = row[NewsTable.content],
                    datePublication = row[NewsTable.datePublication],
                    campaign = campaign
                )
            }
    }
}
