package com.example.tingxie.presentation.quiz_statistics

import com.example.tingxie.domain.model.Character

data class CharacterQuizStatistic (
    val character: Character,
    val correctAnswers: Int,
    val incorrectAnswers: Int
)