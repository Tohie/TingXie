package com.example.tingxie.presentation.character_quiz.components

import com.example.tingxie.domain.model.Character

data class CharacterState (
    val character: Character,
    val isVisibile: Boolean = false,
    val isCorrect: Boolean = false,
)