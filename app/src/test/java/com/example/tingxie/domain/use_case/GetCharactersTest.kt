package com.example.tingxie.domain.use_case

import com.example.tingxie.data.repository.FakeCharacterRepository
import com.example.tingxie.domain.model.Character
import com.google.common.truth.Truth
import kotlinx.coroutines.runBlocking
import org.junit.Test
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.onEach
import org.junit.Before

class GetCharactersTest {
    private lateinit var getCharacters: GetCharacters
    private lateinit var repository: FakeCharacterRepository
    private lateinit var charactersToInsert: MutableList<Character>

    @Before
    fun setUp() {
        repository = FakeCharacterRepository()
        getCharacters = GetCharacters(repository)

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
    fun `GetCharacters will return all characters, returns all`() {
        runBlocking {
            getCharacters.getCharacters().onEach { characters ->
                assertThat(characters).isEqualTo(charactersToInsert)
            }
        }
    }

    @Test
    fun `Get an existing Character, returns the correct Character`() {
        val aCharacter = Character(
            id = 0,
            character = "a",
            pinyin = "a",
            description = "a"
        )
        runBlocking {
            val character = getCharacters.getCharacter(0)
            assertThat(character).isEqualTo(aCharacter)
        }
    }

    @Test
    fun `Get a non existing Character, returns null`() {
        runBlocking {
            val character = getCharacters.getCharacter(100)
            assertThat(character).isEqualTo(null)
        }
    }

    @Test
    fun `getNRandom characters should return correct amount, returns correct amount`() {
        runBlocking {
            getCharacters.getNRandomCharacters(5).onEach { characters ->
                assertThat(characters.size == 5)
            }
        }
    }

    @Test
    fun `getNRandomCharacters should return different each time, returns different characters`() {
        runBlocking {
            getCharacters.getNRandomCharacters(10).onEach { firstCall ->
                runBlocking { getCharacters.getNRandomCharacters(10).onEach { secondCall ->
                    assertThat(firstCall).isNotEqualTo(secondCall)
                } }
            }
        }
    }
}