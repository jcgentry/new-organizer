package com.jacagen.organizer

import io.kvision.annotations.KVService

@KVService
interface IOperationService {
    suspend fun saveOp(op: Operation)
}