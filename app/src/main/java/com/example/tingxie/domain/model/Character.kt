package com.example.tingxie.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Character(
    @PrimaryKey val id: Int?,
    val character: String,
    val pinyin: String,
    val description: String
)

class InvalidCharacterException(message: String): Exception(message)