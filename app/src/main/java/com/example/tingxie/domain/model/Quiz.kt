package com.example.tingxie.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Quiz (
    @PrimaryKey
    val quizId: Int?,
    val timestamp: Long,
    val numberOfCharacters: Int,
    val score: Int
)