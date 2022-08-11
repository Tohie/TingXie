package com.example.tingxie.data.data_source

import androidx.room.*
import com.example.tingxie.domain.model.Character
import kotlinx.coroutines.flow.Flow

@Dao
interface CharacterDao {

    @Query("SELECT * FROM character")
    fun getAll(): Flow<List<Character>>

    @Query("SELECT * FROM character WHERE id IN (:id)")
    suspend fun getId(id: Int): Character?

    @Query("SELECT * FROM character ORDER BY RANDOM() LIMIT (:number)")
    fun getNRandomCharacters(number: Int): Flow<List<Character>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCharacter(character: Character)

    @Delete
    suspend fun deleteCharacter(character: Character)
}