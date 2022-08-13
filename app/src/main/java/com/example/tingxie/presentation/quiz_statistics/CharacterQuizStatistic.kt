package com.example.tingxie.presentation.quiz_statistics

import com.example.tingxie.domain.model.Character

data class CharacterQuizStatistic (
    val character: Character,
    var correctAnswers: Int = 0,
    val incorrectAnswers: Int = 0
)