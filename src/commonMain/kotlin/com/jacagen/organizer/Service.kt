package com.jacagen.organizer

import io.kvision.annotations.KVService
import kotlinx.serialization.Serializable


@Serializable
data class Node(
    val label: String,
    val children: List<Node> = emptyList(),
)





@Serializable
data class MovieCharacter(val name: String, val title: String)

@KVService
interface ICharacterService {
    suspend fun getCharacters(): List<MovieCharacter>
    suspend fun add(movieCharacter: MovieCharacter): Boolean
    suspend fun movies(search: String?, state: String?): List<String>
}
