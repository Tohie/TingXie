package com.example.tingxie.data.data_source

import androidx.room.*
import com.example.tingxie.domain.model.Character
import com.example.tingxie.domain.model.QuizResult
import com.example.tingxie.domain.model.QuizResults
import kotlinx.coroutines.flow.Flow
import java.sql.Timestamp

@Dao
interface CharacterDao {

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

    @Query("SELECT * FROM character JOIN  quizresult ON quizresult.characterIdMap = character.id")
    fun allCharacterResults(): Flow<Map<Character, List<QuizResult>>>

    @Query("SELECT * FROM character JOIN  quizresult ON quizresult.characterIdMap = character.id")
    fun allQuizResults(): Flow<Map<QuizResult, Character>>

    @Query("SELECT * FROM character JOIN  quizresult ON quizresult.characterIdMap = character.id LIMIT (:limit)" )
    fun getQuizResultsLimitedBy(limit: Int): Flow<Map<QuizResult, Character>>

    @Query("SELECT * FROM character JOIN quizresult ON quizresult.characterIdMap = character.id WHERE timestamp IN (:timestamp)")
    fun getQuizResults(timestamp: Long): Flow<Map<QuizResult, Character>>

    @Query("SELECT * FROM character JOIN quizresult ON quizresult.characterIdMap = character.id WHERE timestamp BETWEEN (:start) AND (:end)")
    fun getQuizResultsBetween(start: Long, end: Long): Flow<Map<QuizResult, Character>>

    @Query("SELECT * FROM character JOIN quizresult ON quizresult.characterIdMap = character.id WHERE character IN (:character)")
    fun getCharacterResults(character: String): Flow<Map<Character, List<QuizResult>>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insetQuizResult(quizResult: QuizResult)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuizResults(quizResults: List<QuizResult>)

    @Delete
    suspend fun deleteCharacter(character: Character)
}