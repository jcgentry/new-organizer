package com.jacagen.organizer

import com.jacagen.organizer.Db.dbQuery
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import java.time.ZoneId

actual class OperationService : IOperationService {
    override suspend fun saveOp(op: Operation) {
        val key = dbQuery {
            (OperationDao.insert {
                it[opType] = op.type
            } get OperationDao.id)
        }
        getOp(key!!)
    }

    private suspend fun getOp(id: Int): Operation? = dbQuery {
        OperationDao.select {
            OperationDao.id eq id
        }.mapNotNull { toOp(it) }.single()  // TODO what if it does not exist
    }

    private fun toOp(row: ResultRow): Operation =
        when (row[OperationDao.opType]) {
            "AddNode" -> AddNode(
                id = row[OperationDao.id],
                parent = row[OperationDao.parent],
                node = row[OperationDao.node]!!,
                title = row[OperationDao.title]!!,
                index = row[OperationDao.index]!!)
            "UpdateTitle" -> UpdateTitle(
                id = row[OperationDao.id],
                node = row[OperationDao.node]!!,
                oldTitle = row[OperationDao.oldTitle]!!,
                title = row[OperationDao.title]!!)
            else -> TODO()
        }
}