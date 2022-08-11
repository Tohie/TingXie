package com.example.tingxie.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.tingxie.domain.model.Character

@Database(entities = [Character::class], version = 1)
abstract class CharacterDatabase : RoomDatabase() {
    abstract fun characterDao(): CharacterDao

    companion object {
        const val DATABASE_NAME = "chinese_characters"
    }
}