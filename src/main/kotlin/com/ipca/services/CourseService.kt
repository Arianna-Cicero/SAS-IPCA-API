package com.ipca.services

import com.ipca.models.CourseTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object CourseService {
    fun getAllCourses(): List<Pair<Int, String>> {
        return transaction {
            CourseTable.selectAll().map { it[CourseTable.id] to it[CourseTable.name] }
        }
    }

    fun getById(id: Int): Pair<Int, String>? {
        return transaction {
            CourseTable
                .select { CourseTable.id eq id }
                .singleOrNull()
                ?.let { it[CourseTable.id] to it[CourseTable.name] }
        }
    }

    fun addCourse(name: String) {
        transaction {
            CourseTable.insert {
                it[CourseTable.name] = name
            }
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
