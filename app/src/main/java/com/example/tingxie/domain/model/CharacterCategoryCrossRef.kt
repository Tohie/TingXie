package com.example.tingxie.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(primaryKeys = ["id", "categoryId"])
data class CharacterCategoryCrossRef(
    val id: Int,
    val categoryId: Int
)