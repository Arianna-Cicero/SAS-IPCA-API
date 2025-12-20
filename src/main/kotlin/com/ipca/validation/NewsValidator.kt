package com.ipca.validation

import com.ipca.models.CampaignTable
import com.ipca.dto.News.NewsCreateDTO
import com.ipca.dto.News.NewsUpdateDTO
import com.ipca.exceptions.ValidationException
import com.ipca.exceptions.EntityNotFoundException
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDate

object NewsValidator {
    
    fun validateCreate(dto: NewsCreateDTO) {
        validateTitle(dto.title)
        validateContent(dto.content)
        validatePublicationDate(dto.datePublication)
        if (dto.campaignId != null) validateCampaignExists(dto.campaignId)
    }
    
    fun validateUpdate(dto: NewsUpdateDTO) {
        if (dto.title != null) validateTitle(dto.title)
        if (dto.content != null) validateContent(dto.content)
        if (dto.datePublication != null) validatePublicationDate(dto.datePublication)
        if (dto.campaignId != null) validateCampaignExists(dto.campaignId)
    }
    
    private fun validateTitle(title: String) {
        if (title.isBlank()) {
            throw ValidationException("News title cannot be empty", "news")
        }
        if (title.length < 5 || title.length > 255) {
            throw ValidationException("News title must be between 5 and 255 characters", "news")
        }
    }
    
    private fun validateContent(content: String) {
        if (content.isBlank()) {
            throw ValidationException("News content cannot be empty", "news")
        }
        if (content.length > 5000) {
            throw ValidationException("News content cannot exceed 5000 characters", "news")
        }
    }
    
    private fun validatePublicationDate(publicationDate: LocalDate) {
        if (publicationDate.isAfter(LocalDate.now())) {
            throw ValidationException("Publication date cannot be in the future", "news")
        }
    }
    
    private fun validateCampaignExists(campaignId: Int) {
        val exists = transaction {
            CampaignTable.select { CampaignTable.id eq campaignId }.count() > 0
        }
        
        if (!exists) {
            throw EntityNotFoundException("campaign", campaignId.toString())
        }
    }
}
