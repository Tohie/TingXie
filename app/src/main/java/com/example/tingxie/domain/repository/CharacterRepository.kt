package com.example.tingxie.domain.repository

import com.example.tingxie.domain.model.Character
import kotlinx.coroutines.flow.Flow

interface CharacterRepository {
    fun getCharacters(): Flow<List<Character>>

    suspend fun getCharacter(id: Int): Character?

    suspend fun deleteCharacter(character: Character)

    suspend fun insertCharacter(character: Character)
    fun getNRandomCharacters(number: Int): Flow<List<Character>>
}