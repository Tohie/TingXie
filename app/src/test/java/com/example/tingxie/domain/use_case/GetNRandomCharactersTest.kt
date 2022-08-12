package com.example.tingxie.domain.use_case

import com.example.tingxie.data.repository.FakeCharacterRepository
import com.example.tingxie.domain.model.Character
import kotlinx.coroutines.runBlocking
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.onEach
import org.junit.Before
import org.junit.Test

class GetNRandomCharactersTest {
    private lateinit var getNRandomCharacters: GetNRandomCharacters
    private lateinit var repository: FakeCharacterRepository
    private lateinit var charactersToInsert: MutableList<Character>

    @Before
    fun setUp() {
        repository = FakeCharacterRepository()
        getNRandomCharacters = GetNRandomCharacters(repository)

        charactersToInsert = mutableListOf<Character>()
        ('a'..'z').forEachIndexed { index, c ->
            charactersToInsert.add(
                Character(
                    id = index,
                    character = c.toString(),
                    pinyin = c.toString(),
                    description = c.toString()
                )
            )
        }
        runBlocking {
            charactersToInsert.forEach { char -> repository.insertCharacter(char) }
        }
    }

    @Test
    fun `getNRandom characters should return correct amount, returns correct amount`() {
        runBlocking {
            getNRandomCharacters(5).onEach { characters ->
                assertThat(characters.size == 5)
            }
        }
    }

    @Test
    fun `getNRandomCharacters should return different each time, returns different characters`() {
        runBlocking {
            getNRandomCharacters(10).onEach { firstCall ->
                runBlocking { getNRandomCharacters(10).onEach { secondCall ->
                    assertThat(firstCall).isNotEqualTo(secondCall)
                } }
            }
        }
    }
}