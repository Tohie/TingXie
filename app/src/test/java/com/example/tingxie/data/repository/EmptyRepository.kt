package com.example.tingxie.data.repository

import com.example.tingxie.domain.model.Character
import com.example.tingxie.domain.model.QuizResult
import com.example.tingxie.domain.repository.CharacterRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

// Returns null/nothing to test nothing breaks when empty lists/maps/nulls are returned
class EmptyRepository : CharacterRepository {
    override fun getCharacters(): Flow<List<Character>> {
        return flow { emit(listOf()) }
    }

    override suspend fun getCharacter(id: Int): Character? {
        return null
    }

    override fun getCharactersLike(searchWord: String): Flow<List<Character>> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteCharacter(character: Character) {
        // Do nothing
    }

    override suspend fun insertCharacter(character: Character) {
        // Do nothing
    }

    override fun getNRandomCharacters(number: Int): Flow<List<Character>> {
        return flow { emit(listOf()) }
    }

    override suspend fun insertQuizResult(quizResult: QuizResult) {
        // Do nothing
    }

    override fun getQuizResults(): Flow<Map<QuizResult, Character>> {
        return flow { emit(mapOf()) }
    }

    override fun getCharacterResults(): Flow<Map<Character, List<QuizResult>>> {
        return flow { emit(mapOf())}
    }

    override fun getCharacterResults(character: String): Flow<Map<Character, List<QuizResult>>> {
        return flow { emit(mapOf()) }
    }

    override fun getQuizResult(timestamp: Long): Flow<Map<Character, QuizResult>> {
        return flow { emit(mapOf()) }
    }

    override fun getQuizResultsLimitedBy(limit: Int): Flow<Map<QuizResult, Character>> {
        return flow { emit(mapOf()) }
    }

    override fun getQuizResultBetween(start: Long, end: Long): Flow<Map<QuizResult, Character>> {
        return flow { emit(mapOf()) }
    }
}