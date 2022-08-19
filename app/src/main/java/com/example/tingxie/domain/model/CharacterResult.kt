package com.example.tingxie.domain.model

import androidx.room.Embedded

data class CharacterResult(
    @Embedded
    val character: Character,

    @Embedded
    val quizResult: QuizResult
)
