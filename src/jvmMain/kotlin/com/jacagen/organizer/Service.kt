package com.jacagen.organizer

actual class CharacterService: ICharacterService {
    override suspend fun getCharacters(): List<MovieCharacter> {
        return Repository.getCharacters()
    }

    override suspend fun add(movieCharacter: MovieCharacter): Boolean {
        Repository.add(movieCharacter)
        return true
    }

    override suspend fun movies(search: String?, state: String?): List<String> {
        return Repository.getCharacters().map { it.title }.distinct()
    }
}

object Repository {
    private val characters = mutableListOf<MovieCharacter>()

    fun getCharacters() = characters.also { print("Returning characters $characters") }

    fun add(movieCharacter: MovieCharacter) {
        print("Adding character $movieCharacter")
        characters += movieCharacter
    }
}