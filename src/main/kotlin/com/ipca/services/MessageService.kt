package com.ipca.services

import com.ipca.dto.Message.MessageResponseDTO
import com.ipca.models.MessageTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

object MessageService {
    fun getAllMessages(): List<MessageResponseDTO> {
        return transaction {
            MessageTable.selectAll().map { 
                MessageResponseDTO(
                    id = it[MessageTable.id],
                    name = it[MessageTable.name],
                    email = it[MessageTable.email],
                    category = it[MessageTable.category],
                    message = it[MessageTable.message]
                )
            }
        }
    }

    fun getById(id: Int): MessageResponseDTO? {
        return transaction {
            MessageTable
                .select { MessageTable.id eq id }
                .singleOrNull()
                ?.let { 
                    MessageResponseDTO(
                        id = it[MessageTable.id],
                        name = it[MessageTable.name],
                        email = it[MessageTable.email],
                        category = it[MessageTable.category],
                        message = it[MessageTable.message]
                    )
                }
        }
    }

    fun addMessage(name: String, email: String, category: String, message: String): Int {
        return transaction {
            val insert = MessageTable.insert {
                it[MessageTable.name] = name
                it[MessageTable.email] = email
                it[MessageTable.category] = category
                it[MessageTable.message] = message
            }
            insert.resultedValues?.first()?.get(MessageTable.id)
                ?: error("Failed to insert Message")
        }
    }

    fun delete(id: Int): Boolean {
        return transaction {
            val deleted = MessageTable.deleteWhere { MessageTable.id eq id }
            deleted > 0
        }
    }
}
