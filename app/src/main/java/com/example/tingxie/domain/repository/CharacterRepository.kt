package com.example.tingxie.domain.repository

import com.example.tingxie.domain.model.*
import kotlinx.coroutines.flow.Flow

interface CharacterRepository {
    fun getCharacters(): Flow<List<Character>>

    suspend fun getCharacter(id: Int): Character?

    fun getCharactersLike(searchWord: String): Flow<List<Character>>

    suspend fun deleteCharacter(character: Character)

    suspend fun insertCharacter(character: Character)

    fun getNRandomCharacters(number: Int): Flow<List<Character>>

    suspend fun insertQuizResult(quizResult: QuizResult)

    fun getQuizResults(): Flow<Map<Quiz, List<CharacterResult>>>

    fun getCharacterResults(): Flow<Map<Character, List<CharacterResult>>>

    fun getCharacterResults(character: String): Flow<Map<Character, List<CharacterResult>>>

    fun getQuizResultsLimitedBy(limit: Int): Flow<Map<Quiz, List<CharacterResult>>>

    fun getQuizResultBetween(start: Long, end: Long): Flow<Map<Quiz, List<CharacterResult>>>

    suspend fun insetQuizResults(quizResults: List<QuizResult>)

    fun getQuizResult(quizId: Int): Flow<Map<Quiz, List<CharacterResult>>>

    fun getLatestQuiz(): Flow<Map<Quiz, List<CharacterResult>>>
    suspend fun insertQuiz(quiz: Quiz): Long
    fun getCharacterResultsByQuiz(quizId: Int): Flow<Map<Character, List<CharacterResult>>>
    fun getCharacterResultsLike(searchWord: String): Flow<Map<Character, List<CharacterResult>>>
    fun getCategories(): Flow<List<CategoriesWithCharacters>>
    suspend fun insertCategory(categories: Categories)
    suspend fun addCharacterToCategory(characterCategory: CharacterCategoryCrossRef)
    suspend fun deleteCharacterFromCategory(characterCategory: CharacterCategoryCrossRef)
    fun getCharactersWithCategories(): Flow<List<CharacterWithCategories>>
    fun getCharacterWithCategoriesWithId(id: Int): Flow<List<CharacterWithCategories>>
    suspend fun getCategory(categoryId: Int): Categories?
    fun getCharactersWithCategoriesLike(searchWord: String): Flow<List<CharacterWithCategories>>
}