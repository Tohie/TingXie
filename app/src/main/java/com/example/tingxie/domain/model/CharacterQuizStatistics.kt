package com.example.tingxie.domain.model

import com.example.tingxie.domain.model.Character

data class CharacterQuizStatistics (
    val character: Character,
    var correctAnswers: Int = 0,
    val incorrectAnswers: Int = 0
)