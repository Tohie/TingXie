package com.example.tingxie.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Categories(
    @PrimaryKey
    val categoryId: Int?,
    val categoryName: String
)

class InvalidCategoryException(message: String): Exception(message)
