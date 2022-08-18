package com.example.tingxie.domain.model

import com.example.tingxie.presentation.character_quiz.CharacterQuizEvents

data class QuizResults (
    val character: Character,
    val wasCorrect: Boolean,
)