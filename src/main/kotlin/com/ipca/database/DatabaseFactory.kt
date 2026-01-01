package com.ipca.database

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import io.github.cdimascio.dotenv.dotenv
import com.ipca.models.*

object DatabaseFactory {

    fun init() {
        println("🔍 [DatabaseFactory] Iniciando ligação à base de dados...")

        val dotenv = dotenv { ignoreIfMissing = true }

        // Collect candidates from .env and system environment, then sensible defaults
        val candidates = listOfNotNull(
            // .env DB_*
            dotenv["DB_URL"]?.let { url ->
                Triple(url, dotenv["DB_USER"] ?: System.getenv("DB_USER") ?: "postgres", dotenv["DB_PASS"] ?: System.getenv("DB_PASS") ?: "")
            },
            // System env DB_*
            System.getenv("DB_URL")?.let { url ->
                Triple(url, System.getenv("DB_USER") ?: "postgres", System.getenv("DB_PASS") ?: "")
            },
            // JDBC_* (used in docker-compose api service)
            System.getenv("JDBC_DATABASE_URL")?.let { url ->
                Triple(url, System.getenv("JDBC_DATABASE_USER") ?: "postgres", System.getenv("JDBC_DATABASE_PASSWORD") ?: "")
            },
            // Fallback to localhost defaults
            Triple("jdbc:postgresql://localhost:5432/loja_social_ipca", "postgres", "admin")
        )

        var connected = false
        var lastError: Exception? = null

        for ((url, user, pass) in candidates) {
            try {
                println("▶️ Tentativa de conexão: $url com utilizador '$user'")
                Database.connect(url = url, driver = "org.postgresql.Driver", user = user, password = pass)
                // Touch the connection in a small transaction to validate
                transaction { SchemaUtils.createMissingTablesAndColumns(CourseTable) }
                println("✅ Ligado ao PostgreSQL com sucesso!")
                connected = true
                break
            } catch (e: Exception) {
                println("⚠️ Falha ao conectar com '$url': ${e.message}")
                lastError = e
            }
        }

        if (!connected) {
            println("❌ Falha ao conectar a todas as fontes de configuração de DB.")
            lastError?.printStackTrace()
            return
        }

        // Create full schema once connected
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
    }
}
