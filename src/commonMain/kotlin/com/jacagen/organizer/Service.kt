package com.jacagen.organizer

import io.kvision.annotations.KVService

@KVService
interface IOperationService {
    suspend fun saveOp(op: Operation)
    suspend fun allOps(): List<Operation>   // It would be nice if this was a flow
}