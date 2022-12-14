package com.example.tingxie.di

import android.app.Application
import androidx.room.Room
import com.example.tingxie.data.data_source.CharacterDatabase
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
class TestAppModule {
    @Provides
    @Singleton
    fun provideCharacterDatabase(app: Application): CharacterDatabase {
        return Room.inMemoryDatabaseBuilder(
            app,
            CharacterDatabase::class.java,
        ).build()
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
            getCharacter = GetCharacter(repository),
            getNRandomCharacters = GetNRandomCharacters(repository)
        )
    }
}