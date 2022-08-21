package com.example.tingxie.data.repository

import com.example.tingxie.data.data_source.CharacterDao
import com.example.tingxie.domain.model.*
import com.example.tingxie.domain.repository.CharacterRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CharacterRepositoryImpl @Inject constructor(private val characterDao: CharacterDao): CharacterRepository{
    // Characters
    override fun getCharacters(): Flow<List<Character>> {
        return characterDao.getAll()
    }

    override suspend fun getCharacter(id: Int): Character? {
        return characterDao.getId(id)
    }

    override fun getNRandomCharacters(number: Int): Flow<List<Character>> {
        return characterDao.getNRandomCharacters(number)
    }

    override fun getCharactersLike(searchWord: String): Flow<List<Character>> {
        return characterDao.getCharactersLike(searchWord)
    }

    override suspend fun deleteCharacter(character: Character) {
        characterDao.deleteCharacter(character)
    }

    override suspend fun insertCharacter(character: Character) {
        characterDao.insertCharacter(character)
    }

    // QuizResults
    override suspend fun insertQuizResult(quizResult: QuizResult) {
        characterDao.insetQuizResult(quizResult)
    }

    override fun getQuizResults(): Flow<Map<Quiz, List<CharacterResult>>> {
        return characterDao.getAllQuizResults()
    }

    override fun getCharacterResults(): Flow<Map<Character, List<CharacterResult>>> {
        return characterDao.getCharacterResults()
    }

    override fun getCharacterResultsByQuiz(quizId: Int): Flow<Map<Character, List<CharacterResult>>> {
        return characterDao.getCharacterResultsByQuizId(quizId)
    }


    override fun getQuizResultsLimitedBy(limit: Int): Flow<Map<Quiz, List<CharacterResult>>> {
        return characterDao.getQuizResultsLimitedBy(limit)
    }

    override fun getCharacterResults(character: String): Flow<Map<Character, List<CharacterResult>>> {
        return characterDao.getCharacterResults(character = character)
    }

    override fun getCharacterResultsLike(searchWord: String): Flow<Map<Character, List<CharacterResult>>> {
        return characterDao.getCharacterResults(searchWord)
    }

    override suspend fun insetQuizResults(quizResults: List<QuizResult>) {
        characterDao.insertQuizResults(quizResults = quizResults)
    }

    // Quizzes
    override fun getQuizResult(quizId: Int): Flow<Map<Quiz, List<CharacterResult>>> {
        return characterDao.getQuizResult(quizId)
    }

    override fun getQuizResultBetween(start: Long, end: Long): Flow<Map<Quiz, List<CharacterResult>>> {
        return characterDao.getQuizResultsBetween(start, end)
    }

    override fun getLatestQuiz(): Flow<Map<Quiz, List<CharacterResult>>> {
        return characterDao.getLatestQuiz()
    }

    override suspend fun insertQuiz(quiz: Quiz): Long {
        return characterDao.insertQuiz(quiz)
    }

    // Categories
    override fun getCategories(): Flow<List<CategoriesWithCharacters>> {
        return characterDao.getCategories()
    }

    override suspend fun insertCategory(categories: Categories) {
        return characterDao.insertCategory(categories)
    }

    override suspend fun addCharacterToCategory(characterCategory: CharacterCategoryCrossRef)  {
        return characterDao.addCharacterToCategory(characterCategory)
    }

    override suspend fun deleteCharacterFromCategory(characterCategory: CharacterCategoryCrossRef) {
        return characterDao.deleteCharacterFromCategory(characterCategory)
    }
}