package com.example.tingxie.domain.use_case

import com.example.tingxie.data.repository.FakeCharacterRepository
import com.example.tingxie.domain.model.Character
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.runBlocking

import org.junit.Before
import org.junit.Test

class DeleteCharacterTest {
    private lateinit var deleteCharacter: DeleteCharacter
    private lateinit var repository: FakeCharacterRepository

    @Before
    fun setUp() {
        repository = FakeCharacterRepository()
        deleteCharacter = DeleteCharacter(repository)

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
    fun `delete a Character that exists, character is deleted`() {
        val character = Character(
            id = 0,
            character = 'a'.toString(),
            pinyin = 'a'.toString(),
            description = 'a'.toString()
        )
        runBlocking {
            deleteCharacter(character)
            runBlocking {  repository.getCharacters() }.onEach { characters ->
                assertThat(character).isNotIn(characters)
            }
        }
    }

    @Test
    fun `delete a Character that doesn't exist, characters is unaltered`() {
        val character = Character(
            id = 0,
            character = "aa",
            pinyin = "aa",
            description = "aa"
        )
        runBlocking {
            runBlocking { repository.getCharacters() }.onEach { originalCharacters ->
                deleteCharacter(character)
                runBlocking {  repository.getCharacters() }.onEach { characters ->
                    assertThat(characters).isEqualTo(originalCharacters)
                }
            }
        }
    }
}