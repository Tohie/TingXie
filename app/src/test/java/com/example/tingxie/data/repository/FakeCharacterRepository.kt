package com.example.tingxie.data.repository

import com.example.tingxie.domain.model.Character
import com.example.tingxie.domain.repository.CharacterRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeCharacterRepository : CharacterRepository {
    private val characters = mutableListOf<Character>()

    override fun getCharacters(): Flow<List<Character>> {
        return flow { emit(characters) }
    }

    override suspend fun getCharacter(id: Int): Character? {
        return characters.find { char -> char.id == id }
    }

    override suspend fun deleteCharacter(character: Character) {
        characters.remove(character)
    }

    override suspend fun insertCharacter(character: Character) {
        characters.add(character)
    }

    override fun getNRandomCharacters(number: Int): Flow<List<Character>> {
        val copy: MutableList<Character> = mutableListOf()
        copy.addAll(characters)

        copy.shuffle()

        return flow { emit(copy.take(number))}
    }
}