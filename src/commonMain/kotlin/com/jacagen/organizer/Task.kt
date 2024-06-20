package com.jacagen.organizer

import kotlinx.serialization.Serializable

@Serializable
data class Task(val id: Guid, var label: String) {
    override fun toString() = "\"" + label + "\""
}