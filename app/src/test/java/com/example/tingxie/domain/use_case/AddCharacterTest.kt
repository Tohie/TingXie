package com.example.tingxie.domain.use_case

import com.example.tingxie.data.repository.FakeCharacterRepository
import com.example.tingxie.domain.model.Character
import com.example.tingxie.domain.model.InvalidCharacterException
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class AddCharacterTest {
    private lateinit var addCharacter: AddCharacter
    private lateinit var repository: FakeCharacterRepository

    @Before
    fun setUp() {
        repository = FakeCharacterRepository()
        addCharacter = AddCharacter(repository)

        val charactersToInsert = mutableListOf<Character>()
        ('a'..'z').forEachIndexed { index, c ->
            charactersToInsert.add(
                Character(
                    id = index,
                    character = c.toString(),
                    pinyin = c.toString(),
                    description = c.toString(),
                    characterNumber = index
                )
            )
        }
        runBlocking {
            charactersToInsert.forEach { char -> repository.insertCharacter(char) }
        }
    }

    @Test
    fun `Add Valid Characters, adds character`() = runBlocking {
        val addChinese = Character(
            id = 27,
            character = "你",
            pinyin = "You",
            description = "To have",
            characterNumber = 27
        )

        addCharacter(addChinese)
    }

    @Test
    fun `Add Character with no title, throws exception`() = runBlocking {
        val noTitle = Character(
            id = 27,
            character = "",
            pinyin = "you",
            description = "has",
            characterNumber = 27
        )
        try {
            addCharacter(noTitle)
        } catch (e: InvalidCharacterException) {
            assertThat(e).hasMessageThat().contains("Character can't be blank")
        }
    }

    @Test
    fun `Add Character with no pinyin, throws exception`() = runBlocking {
        val noTitle = Character(
            id = 27,
            character = "莫",
            pinyin = "",
            description = "has",
            characterNumber = 27
        )
        try {
            addCharacter(noTitle)
        } catch (e: InvalidCharacterException) {
            assertThat(e).hasMessageThat().contains("Pinyin can't be blank")
        }
    }

    @Test
    fun `Add Character with no description, throws exception`() = runBlocking {
        val noTitle = Character(
            id = 27,
            character = "莫",
            pinyin = "ni",
            description = "",
            characterNumber = 27
        )
        try {
            addCharacter(noTitle)
        } catch (e: InvalidCharacterException) {
            assertThat(e).hasMessageThat().contains("Description can't be blank")
        }
    }

    @Test
    fun `Add Character with no id, adds Character`() = runBlocking {
        val noTitle = Character(
            id = null,
            character = "莫",
            pinyin = "ni",
            description = "has",
            characterNumber = 27
        )

        addCharacter(noTitle)
    }
}