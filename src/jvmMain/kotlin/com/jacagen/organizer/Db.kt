package com.jacagen.organizer

import io.ktor.server.config.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils.create
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.transaction

object Db {
    fun init(config: ApplicationConfig) {
//        Database.connect(hikari(config))
//        transaction {
//            create(OperationDao)
//        }
    }

    // TODO
//    private fun hikari(config: ApplicationConfig): HikariDataSource {
//        val hikariConfig = HikariConfig()
//        hikariConfig.driverClassName = config.propertyOrNull("db.driver")?.getString() ?: "org.h2.Driver"
//        hikariConfig.jdbcUrl = config.propertyOrNull("db.jdbcUrl")?.getString() ?: "jdbc:h2:mem:test"
//        hikariConfig.username = config.propertyOrNull("db.username")?.getString()
//        hikariConfig.password = config.propertyOrNull("db.password")?.getString()
//        hikariConfig.maximumPoolSize = 3
//        hikariConfig.isAutoCommit = false
//        hikariConfig.transactionIsolation = "TRANSACTION_REPEATABLE_READ"
//        hikariConfig.validate()
//        return HikariDataSource(hikariConfig)
//    }

    suspend fun <T> dbQuery(block: Transaction.() -> T): T = withContext(Dispatchers.IO) {
        transaction {
            block()
        }
    }
}