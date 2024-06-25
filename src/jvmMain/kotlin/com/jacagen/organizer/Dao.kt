package com.jacagen.organizer

import org.jetbrains.exposed.sql.Table

object OperationDao : Table("operations") {
    val id = integer("id").primaryKey().autoIncrement()
    val opType = varchar("op_type", 255)
}