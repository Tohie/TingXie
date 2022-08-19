package com.example.tingxie.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.tingxie.domain.model.Character
import com.example.tingxie.domain.model.QuizResult
import com.example.tingxie.domain.model.Quiz

@Database(entities = [Character::class, QuizResult::class, Quiz::class], version = 3)
abstract class CharacterDatabase : RoomDatabase() {
    abstract fun characterDao(): CharacterDao

    companion object {
        const val DATABASE_NAME = "chinese_characters"
    }
}

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE character ADD COLUMN characterNumber INTEGER NOT NULL DEFAULT 0")
        database.execSQL("UPDATE character SET characterNumber = id")
    }
}