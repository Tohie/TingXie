package com.example.tingxie.domain.model

import android.util.Log
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Timestamp

@Entity
data class Quiz (
    @PrimaryKey
    val quizId: Int?,
    val timestamp: Long,
    val numberOfCharacters: Int,
    val score: Int
)