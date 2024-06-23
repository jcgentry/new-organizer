package com.jacagen.organizer

import kotlinx.serialization.Serializable

@Serializable
data class Task(val id: Guid, var title: String) {
    override fun toString() = "$id: \"$title\""
}