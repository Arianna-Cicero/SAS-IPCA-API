package com.ipca.database

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import io.github.cdimascio.dotenv.dotenv
import com.ipca.models.*

object DatabaseFactory {

    fun init() {
        println("🔍 [DatabaseFactory] Iniciando ligação à base de dados...")

        val dotenv = dotenv {
            ignoreIfMissing = false
        }

        val dbUrl = dotenv["DB_URL"]
        val dbUser = dotenv["DB_USER"]
        val dbPass = dotenv["DB_PASS"]

        if (dbUrl.isNullOrBlank()) {
            println("❌ ERRO: DB_URL não encontrado! Verifica o ficheiro .env na raiz do projeto.")
            return
        }

        try {
            Database.connect(
                    url = dbUrl,
                    driver = "org.postgresql.Driver",
                    user = dbUser ?: "postgres",
                    password = dbPass ?: ""
            )
            println("✅ Ligado ao PostgreSQL com sucesso!")

            transaction {
                SchemaUtils.create(
                        CourseTable,
                        CollaboratorTable,
                        BeneficiaryTable,
                        SchedulingTable,
                        DeliveryTable,
                        DonationTable,
                        GoodTable,
                        ExpirationAlertTable,
                        DeliveryItemTable,
                        CampaignTable,
                        NewsTable
                )
            }
        } catch (e: Exception) {
            println("❌ Falha ao conectar: ${e.message}")
            e.printStackTrace()
        }
    }
}
