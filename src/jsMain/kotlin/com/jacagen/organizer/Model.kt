package com.jacagen.organizer

object Model {
    private val operationService = OperationService()

    // TODO security

    suspend fun saveOp(op: Operation) {
        operationService.saveOp(op)
    }

    suspend fun allOps() =
        operationService.allOps()
}