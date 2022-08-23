package com.example.tingxie.data.repository

import com.example.tingxie.domain.model.Character
import com.example.tingxie.domain.model.QuizResult
import com.example.tingxie.domain.repository.CharacterRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class FakeCharacterRepository : CharacterRepository {
    private val characters = mutableListOf<Character>()
    private val quizResults = mutableListOf<QuizResult>()

    override suspend fun getCharacters(): Flow<List<Character>> {
        return flow { emit(characters) }
    }

    override suspend fun getCharacter(id: Int): Character? {
        return characters.find { char -> char.id == id }
    }

    override fun getCharactersLike(searchWord: String): Flow<List<Character>> {
        TODO("Not yet implemented")
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

    override suspend fun insertQuizResult(quizResult: QuizResult) {
        quizResults.add(quizResult)
    }

    override fun getQuizResults(): Flow<Map<QuizResult, Character>> {
        val results: MutableMap<QuizResult, Character> = mutableMapOf()
        for (result in quizResults) {
            val charId = result.characterIdMap
            val char = characters.find { it.id == charId } ?: break
            results[result] = char
        }
        return flow { emit(results) }
    }

    override suspend fun getCharacterResults(): Flow<Map<Character, List<QuizResult>>> {
        val results: MutableMap<Character, List<QuizResult>> = mutableMapOf()
        for (char in characters) {
            val charResults = quizResults.filter { it.characterIdMap == char.id }
            results[char] = charResults
        }
        return flow { emit(results) }
    }

    override fun getCharacterResults(character: String): Flow<Map<Character, List<QuizResult>>> {
        return getCharacterResults().map { results ->
            results.filterKeys { it.character == character }
        }
    }

    override fun getQuizResult(timestamp: Long): Flow<Map<Character, QuizResult>> {
        TODO("Not yet implemented")
    }

    override fun getQuizResultsLimitedBy(limit: Int): Flow<Map<QuizResult, Character>> {
        return getQuizResults().map {
            it.toList().take(limit).toMap()
        }
    }

    override fun getQuizResultBetween(start: Long, end: Long): Flow<Map<QuizResult, Character>> {
        return getQuizResults().map {
            it.filterKeys { it.timestamp in start..end }
        }
    }
}