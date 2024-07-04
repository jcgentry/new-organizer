package com.jacagen.organizer

import org.jetbrains.exposed.sql.Table

object OperationDao : Table("operations") {
    val id = integer("id").primaryKey().autoIncrement()
    val opType = varchar("op_type", 255)

    // TODO Union of all fields--yuck
    val parent = varchar("parent", 255).nullable()  // What is real size of Guid?
    val title = varchar("title", 255).nullable()  // This needs to be bigger
    val index = integer("index").nullable()
    val node = varchar("node", 255).nullable()
    val oldTitle = varchar("old_title", 255).nullable()
}