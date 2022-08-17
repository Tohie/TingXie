package com.example.tingxie.domain.repository

import com.example.tingxie.domain.model.Character
import com.example.tingxie.domain.model.QuizResult
import kotlinx.coroutines.flow.Flow

interface CharacterRepository {
    fun getCharacters(): Flow<List<Character>>

    suspend fun getCharacter(id: Int): Character?

    fun getCharactersLike(searchWord: String): Flow<List<Character>>

    suspend fun deleteCharacter(character: Character)

    suspend fun insertCharacter(character: Character)

    fun getNRandomCharacters(number: Int): Flow<List<Character>>

    suspend fun insertQuizResult(quizResult: QuizResult)

    fun getQuizResults(): Flow<Map<QuizResult, Character>>

    fun getCharacterResults(character: String): Flow<Map<QuizResult, Character>>

    fun getQuizResult(timestamp: Long): Flow<Map<QuizResult, Character>>

    fun getQuizResultsLimitedBy(limit: Int): Flow<Map<QuizResult, Character>>

    fun getQuizResultBetween(start: Long, end: Long): Flow<Map<QuizResult, Character>>
}