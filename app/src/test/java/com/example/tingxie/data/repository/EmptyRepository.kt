package com.example.tingxie.data.repository

import com.example.tingxie.domain.model.Character
import com.example.tingxie.domain.model.CharacterResult
import com.example.tingxie.domain.model.Quiz
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
        return flow { emit (emptyList()) }
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

    override fun getQuizResults(): Flow<Map<Quiz, List<CharacterResult>>> {
        return flow { emit(mapOf()) }
    }

    override fun getCharacterResults(): Flow<Map<Character, List<CharacterResult>>> {
        return flow { emit(mapOf())}
    }

    override fun getCharacterResults(character: String): Flow<Map<Character, List<CharacterResult>>> {
        return flow { emit(mapOf()) }
    }

    override fun getQuizResultsLimitedBy(limit: Int): Flow<Map<Quiz, List<CharacterResult>>> {
        return flow { emit(mapOf()) }
    }

    override fun getQuizResultBetween(start: Long, end: Long): Flow<Map<Quiz, List<CharacterResult>>> {
        return flow { emit(mapOf()) }
    }

    override suspend fun insetQuizResults(quizResults: List<QuizResult>) {
        // Do nothing
    }

    override fun getQuizResult(quizId: Int): Flow<Map<Quiz, List<CharacterResult>>> {
        return flow { emit(mapOf()) }
    }

    override fun getLatestQuiz(): Flow<Map<Quiz, List<CharacterResult>>> {
        return flow { emit(mapOf()) }
    }

    override suspend fun insertQuiz(quiz: Quiz): Long {
        return -1
    }

    override fun getCharacterResultsByQuiz(quizId: Int): Flow<Map<Character, List<CharacterResult>>> {
        return flow { emit(mapOf()) }
    }

    override fun getCharacterResultsLike(searchWord: String): Flow<Map<Character, List<CharacterResult>>> {
        return flow { emit(emptyMap()) }
    }
}