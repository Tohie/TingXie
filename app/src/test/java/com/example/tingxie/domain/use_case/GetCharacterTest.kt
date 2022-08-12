package com.example.tingxie.domain.use_case

import com.example.tingxie.data.repository.FakeCharacterRepository
import com.example.tingxie.domain.model.Character
import kotlinx.coroutines.runBlocking
import com.google.common.truth.Truth.assertThat
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class GetCharacterTest {
    private lateinit var getCharacter: GetCharacter
    private lateinit var repository: FakeCharacterRepository

    @Before
    fun setUp() {
        repository = FakeCharacterRepository()
        getCharacter = GetCharacter(repository)

        val charactersToInsert = mutableListOf<Character>()
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
    fun `Get an existing Character, returns the correct Character`() {
        val aCharacter = Character(
            id = 0,
            character = "a",
            pinyin = "a",
            description = "a"
        )
        runBlocking {
            val character = getCharacter(0)
            assertThat(character).isEqualTo(aCharacter)
        }
    }

    @Test
    fun `Get a non existing Character, returns null`() {
        runBlocking {
            val character = getCharacter(100)
            assertThat(character).isEqualTo(null)
        }
    }
}