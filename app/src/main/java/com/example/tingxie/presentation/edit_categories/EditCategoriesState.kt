package com.example.tingxie.presentation.edit_categories

import com.example.tingxie.domain.model.Categories
import com.example.tingxie.domain.model.Character
import com.example.tingxie.domain.model.CharacterCategoryCrossRef

data class EditCategoriesState(
    val allCategories: List<Categories> = listOf(),
    val currentCategories: List<Categories> = listOf(),
    val lastDeletedCategory: CharacterCategoryCrossRef? = null,
    val character: Character? = null
)