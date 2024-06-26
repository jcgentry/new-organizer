package com.jacagen.organizer

object Model {
    private val operationService = OperationService()

    // TODO security

    suspend fun save(op: Operation) {
        operationService.save(op)
    }
}