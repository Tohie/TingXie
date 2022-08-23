package com.example.tingxie.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Timestamp

@Entity
data class QuizResult(
    @PrimaryKey
    val resultId: Int?,
    val isCorrect: Boolean,
    val timestamp: Long,
    val characterIdMap: Int,
    val quizResultsIdMap: Int
)
