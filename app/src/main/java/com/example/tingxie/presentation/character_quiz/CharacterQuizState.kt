package com.example.tingxie.presentation.character_quiz

import com.example.tingxie.domain.model.Categories
import com.example.tingxie.presentation.character_quiz.components.CharacterState

data class CharacterQuizState(
    val characters: List<CharacterState> = mutableListOf(),
    val currentCharacter: Int = 0,
    val numberOfCharacters: Int = 5,
    val isQuitWithoutSavingDialogueVisible: Boolean = false,
    val categories: List<Categories> = listOf(),
    val currentCategory: Categories? = null
)