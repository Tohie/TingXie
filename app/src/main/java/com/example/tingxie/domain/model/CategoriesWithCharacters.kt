package com.example.tingxie.domain.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class CategoriesWithCharacters(
    @Embedded val category: Categories,
    @Relation(
        parentColumn = "categoryId",
        entityColumn = "id",
        associateBy = Junction(CharacterCategoryCrossRef::class)
    )
    val characters: List<Character>
)
