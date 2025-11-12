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

    fun addCourse(name: String) {
        transaction {
            CourseTable.insert {
                it[CourseTable.name] = name
            }
        }
    }
}
