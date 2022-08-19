package com.example.tingxie.domain.model

import androidx.room.Embedded

data class CharacterStatistics(
    @Embedded
    val character: Character,

    val correctAnswers: Int,
    val incorrectAnswers: Int
)