package com.jacagen.organizer

import com.jacagen.organizer.Db.dbQuery
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.slf4j.LoggerFactory


actual class OperationService : IOperationService {
    val logger = LoggerFactory.getLogger(OperationService::class.java)

    override suspend fun saveOp(op: Operation) {
        logger.debug("Saving operation {}", op)
        val key = dbQuery {
            (OperationDao.insert {
                it[opType] = op.opType
                when (op.opType) {
                    "AddNode" -> {
                        val addNode = op as AddNode
                        it[parent] = addNode.parent
                        it[node] = addNode.node
                        it[title] = addNode.title
                        it[index] = addNode.index
                    }

                    "UpdateTitle" -> {
                        val updateTitle = op as UpdateTitle
                        it[node] = updateTitle.node
                        it[oldTitle] = updateTitle.oldTitle
                        it[title] = updateTitle.title
                    }
                }
            } get OperationDao.id)
        }
        getOp(key)
    }

    override suspend fun allOps(): List<Operation> = dbQuery {
        // TODO Isn't there a way to avoid making this a list?
        OperationDao.selectAll().mapNotNull { toOp(it) }
    }

    private suspend fun getOp(id: Int): Operation = dbQuery {
        OperationDao.select {
            OperationDao.id eq id
        }.mapNotNull { toOp(it) }.single()  // What if it does not exist?
    }

    private fun toOp(row: ResultRow): Operation = when (row[OperationDao.opType]) {
        "AddNode" -> AddNode(
            id = row[OperationDao.id],
            parent = row[OperationDao.parent],
            node = row[OperationDao.node]!!,
            title = row[OperationDao.title]!!,
            index = row[OperationDao.index]
        )

        "UpdateTitle" -> UpdateTitle(
            id = row[OperationDao.id],
            node = row[OperationDao.node]!!,
            oldTitle = row[OperationDao.oldTitle]!!,
            title = row[OperationDao.title]!!
        )

        else -> TODO()
    }
}