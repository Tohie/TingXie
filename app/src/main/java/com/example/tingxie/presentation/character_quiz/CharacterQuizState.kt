package com.example.tingxie.presentation.character_quiz

import com.example.tingxie.domain.model.Character

data class CharacterQuizState(
    val correctAnswers: Int = 0,
    val characters: MutableList<Character> = mutableListOf(),
    val currentCharacter: Character? = null,
    val isCharacterVisible: Boolean = false
)