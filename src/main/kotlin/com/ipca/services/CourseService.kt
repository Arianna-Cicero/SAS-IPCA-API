package com.ipca.services

import com.ipca.dto.Course.CourseResponseDTO
import com.ipca.models.CourseTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

object CourseService {
    fun getAllCourses(): List<CourseResponseDTO> {
        return transaction {
            CourseTable.selectAll().map { 
                CourseResponseDTO(
                    id = it[CourseTable.id],
                    name = it[CourseTable.name]
                )
            }
        }
    }

    fun getById(id: Int): CourseResponseDTO? {
        return transaction {
            CourseTable
                .select { CourseTable.id eq id }
                .singleOrNull()
                ?.let { 
                    CourseResponseDTO(
                        id = it[CourseTable.id],
                        name = it[CourseTable.name]
                    )
                }
        }
    }

    fun addCourse(name: String): Int {
        return transaction {
            val insert = CourseTable.insert {
                it[CourseTable.name] = name
            }
            insert.resultedValues?.first()?.get(CourseTable.id)
                ?: error("Failed to insert Course")
        }
    }

    fun update(id: Int, name: String) {
        transaction {
            CourseTable.update({ CourseTable.id eq id }) { row ->
                row[CourseTable.name] = name
            }
        }
    }

    fun delete(id: Int) {
        transaction {
            CourseTable.deleteWhere { CourseTable.id eq id }
        }
    }
}
