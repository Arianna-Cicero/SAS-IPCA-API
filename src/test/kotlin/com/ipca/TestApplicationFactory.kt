package com.ipca

import com.ipca.database.DatabaseFactory
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

object TestApplicationFactory {
    
    fun setupTestDatabase() {
        // Use in-memory H2 database for testing
        Database.connect(
            url = "jdbc:h2:mem:test;MODE=PostgreSQL",
            driver = "org.h2.Driver"
        )
        
        // Create all tables
        transaction {
            SchemaUtils.create(
                com.ipca.models.CollaboratorTable,
                com.ipca.models.SessionTable,
                com.ipca.models.EntityTable,
                com.ipca.models.CourseTable,
                com.ipca.models.BeneficiaryTable,
                com.ipca.models.CampaignTable,
                com.ipca.models.DeliveryTable,
                com.ipca.models.DeliveryItemTable,
                com.ipca.models.DonationTable,
                com.ipca.models.GoodTable,
                com.ipca.models.NewsTable,
                com.ipca.models.SchedulingTable,
                com.ipca.models.ExpirationAlertTable,
                com.ipca.models.ActivityLogTable
            )
        }
    }
    
    fun createTestClient(block: suspend (TestApplicationEngine) -> Unit) {
        setupTestDatabase()
        testApplication {
            application {
                module()
            }
            block(this)
        }
    }
}

suspend inline fun <reified T> HttpResponse.parseBody(): T {
    return io.ktor.client.call.body()
}
