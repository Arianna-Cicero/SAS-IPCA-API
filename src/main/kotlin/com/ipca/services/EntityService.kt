package com.ipca.services

import com.ipca.dto.Entity.EntityCreateDTO
import com.ipca.dto.Entity.EntityResponseDTO
import com.ipca.models.EntityTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

object EntityService {

    fun getAll(): List<EntityResponseDTO> = transaction {
        EntityTable.selectAll().map { row ->
            EntityResponseDTO(
                id = row[EntityTable.id],
                name = row[EntityTable.name]
            )
        }
    }

    fun getById(id: Int): EntityResponseDTO? = transaction {
        EntityTable
            .select { EntityTable.id eq id }
            .singleOrNull()
            ?.let { row ->
                EntityResponseDTO(
                    id = row[EntityTable.id],
                    name = row[EntityTable.name]
                )
            }
    }

    fun create(request: EntityCreateDTO): Int = transaction {
        val insert = EntityTable.insert { row ->
            row[name] = request.name
        }
        insert.resultedValues?.first()?.get(EntityTable.id)
            ?: error("Failed to insert Entity")
    }

    fun getByName(name: String): EntityResponseDTO? = transaction {
        EntityTable
            .select { EntityTable.name eq name }
            .singleOrNull()
            ?.let { row ->
                EntityResponseDTO(
                    id = row[EntityTable.id],
                    name = row[EntityTable.name]
                )
            }
    }
}
