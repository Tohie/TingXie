package com.example.tingxie.presentation.character_quiz

import com.example.tingxie.domain.model.Character
import com.example.tingxie.presentation.character_quiz.components.CharacterState
import kotlinx.coroutines.flow.MutableStateFlow

data class CharacterQuizState(
    val characters: List<CharacterState> = mutableListOf<CharacterState>(),
    val currentCharacter: Int = 0,
    val numberOfCharacters: Int = 0,
)