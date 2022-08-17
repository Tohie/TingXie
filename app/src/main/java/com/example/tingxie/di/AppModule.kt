package com.example.tingxie.di

import android.app.Application
import androidx.room.Room
import com.example.tingxie.data.data_source.CharacterDatabase
import com.example.tingxie.data.data_source.MIGRATION_1_2
import com.example.tingxie.data.repository.CharacterRepositoryImpl
import com.example.tingxie.domain.repository.CharacterRepository
import com.example.tingxie.domain.use_case.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun provideCharacterDatabase(app: Application): CharacterDatabase {
        return Room.databaseBuilder(
            app,
            CharacterDatabase::class.java,
            CharacterDatabase.DATABASE_NAME
        )
            .addMigrations(MIGRATION_1_2)
            .build()
    }

    @Provides
    @Singleton
    fun provideCharacterRepository(db: CharacterDatabase): CharacterRepository {
        return CharacterRepositoryImpl(db.characterDao())
    }

    @Provides
    @Singleton
    fun provideCharacterUseCases(repository: CharacterRepository): CharacterUseCases {
        return CharacterUseCases(
            addCharacter = AddCharacter(repository),
            deleteCharacter = DeleteCharacter(repository),
            getCharacters = GetCharacters(repository),
            insertQuizResult = InsertQuizResult(repository),
            getQuizResults = GetQuizResults(repository),
        )
    }
}