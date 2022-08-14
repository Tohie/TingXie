package com.example.tingxie.data.repository

import com.example.tingxie.data.data_source.CharacterDao
import com.example.tingxie.domain.model.Character
import com.example.tingxie.domain.model.QuizResult
import com.example.tingxie.domain.repository.CharacterRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CharacterRepositoryImpl @Inject constructor(private val characterDao: CharacterDao): CharacterRepository{
    override fun getCharacters(): Flow<List<Character>> {
        return characterDao.getAll()
    }

    override suspend fun getCharacter(id: Int): Character? {
        return characterDao.getId(id)
    }

    override fun getNRandomCharacters(number: Int): Flow<List<Character>> {
        return characterDao.getNRandomCharacters(number)
    }

    override suspend fun insertQuizResult(quizResult: QuizResult) {
        characterDao.insetQuizResult(quizResult)
    }

    override fun getQuizResults(): Flow<Map<QuizResult, Character>> {
        return characterDao.allCharacterResults()
    }

    override fun getQuizResultsLimitedBy(limit: Int): Flow<Map<QuizResult, Character>> {
        return characterDao.allCharacterResultsLimitedBy(limit)
    }

    override fun getCharacterResults(character: String): Flow<Map<QuizResult, Character>> {
        return characterDao.getCharacterResult(character = character)
    }

    override fun getQuizResult(timestamp: Long): Flow<Map<QuizResult, Character>> {
        return characterDao.getQuizResults(timestamp)
    }

    override suspend fun deleteCharacter(character: Character) {
        characterDao.deleteCharacter(character)
    }

    override suspend fun insertCharacter(character: Character) {
        characterDao.insertCharacter(character)
    }

}