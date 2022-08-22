package com.example.tingxie.domain.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class CharacterWithCategories (
    @Embedded val character: Character,
    @Relation(
        parentColumn = "id",
        entityColumn = "categoryId",
        associateBy = Junction(CharacterCategoryCrossRef::class)
    )
    val categories: List<Categories>
)