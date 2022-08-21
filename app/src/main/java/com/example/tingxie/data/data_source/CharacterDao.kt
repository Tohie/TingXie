package com.example.tingxie.data.data_source

import androidx.room.*
import com.example.tingxie.domain.model.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CharacterDao {
    // Characters
    @Query("SELECT * FROM character")
    fun getAll(): Flow<List<Character>>

    @Query("SELECT * FROM character WHERE character LIKE '%' || :searchWord || '%'")
    fun getCharactersLike(searchWord: String): Flow<List<Character>>

    @Query("SELECT * FROM character WHERE id IN (:id)")
    suspend fun getId(id: Int): Character?

    @Query("SELECT * FROM character ORDER BY RANDOM() LIMIT (:number)")
    fun getNRandomCharacters(number: Int): Flow<List<Character>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCharacter(character: Character)

    @Delete
    suspend fun deleteCharacter(character: Character)

    // QuizResults
    @Query("SELECT * FROM character JOIN  quizresult ON quizresult.characterIdMap = character.id")
    fun getCharacterResults(): Flow<Map<Character, List<CharacterResult>>>

    @Query("SELECT * FROM character JOIN  quizresult ON quizresult.characterIdMap = character.id  WHERE character LIKE '%' || :searchWord || '%'")
    fun getCharacterResultsLike(searchWord: String): Flow<Map<Character, List<CharacterResult>>>


    @Query("SELECT * FROM character JOIN  quizresult ON quizresult.characterIdMap = character.id WHERE quizResultsIdMap IN (:quizId)")
    fun getCharacterResultsByQuizId(quizId: Int): Flow<Map<Character, List<CharacterResult>>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insetQuizResult(quizResult: QuizResult)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuizResults(quizResults: List<QuizResult>)

    @Query("SELECT * FROM character JOIN quizresult ON quizresult.characterIdMap = character.id WHERE character IN (:character)")
    fun getCharacterResults(character: String): Flow<Map<Character, List<CharacterResult>>>

    // Quizzes
    @Query("SELECT * FROM quiz JOIN quizresult ON quizResultsIdMap = quizId JOIN character ON characterIdMap = character.id")
    fun getAllQuizResults(): Flow<Map<Quiz, List<CharacterResult>>>

    @Query("SELECT * FROM quiz JOIN quizresult ON quizResultsIdMap = quizId JOIN character ON characterIdMap = character.id WHERE quizId IS (:quizId)")
    fun getQuizResult(quizId: Int): Flow<Map<Quiz, List<CharacterResult>>>


    @Query("SELECT * FROM quiz JOIN quizresult ON quizResultsIdMap = quizId JOIN character ON characterIdMap = character.id ORDER BY quiz.timestamp DESC LIMIT 1")
    fun getLatestQuiz(): Flow<Map<Quiz, List<CharacterResult>>>

    @Query("SELECT * FROM quiz JOIN quizresult ON quizResultsIdMap = quizId JOIN character ON characterIdMap = character.id LIMIT (:limit)")
    fun getQuizResultsLimitedBy(limit: Int): Flow<Map<Quiz, List<CharacterResult>>>

    @Query("SELECT * FROM quiz JOIN quizresult ON quizResultsIdMap = quizId JOIN character ON characterIdMap = character.id WHERE quiz.timestamp BETWEEN (:start) AND (:end)")
    fun getQuizResultsBetween(start: Long, end: Long): Flow<Map<Quiz, List<CharacterResult>>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuiz(quiz: Quiz): Long

    @Query("SELECT * FROM quiz JOIN quizresult ON quizResultsIdMap = quizId JOIN character ON characterIdMap = character.id")
    fun getQuizzes(): Flow<Map<Quiz, List<CharacterResult>>>

    // Categories
    @Transaction
    @Query("SELECT * FROM categories")
    fun getCategories(): Flow<List<CategoriesWithCharacters>>

    @Insert
    suspend fun insertCategory(categories: Categories)

    @Insert
    suspend fun addCharacterToCategory(characterCategory: CharacterCategoryCrossRef)

    @Delete
    suspend fun deleteCharacterFromCategory(characterCategory: CharacterCategoryCrossRef)
}