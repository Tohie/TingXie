package com.example.tingxie.presentation.edit_categories

import com.example.tingxie.domain.model.Categories
import com.example.tingxie.presentation.edit_character.EditCharacterEvent

sealed class EditCategoriesEvents {
    data class DeleteCharacterCategoryCrossRef(val category: Categories) : EditCategoriesEvents()
    object UndoLastDeleted : EditCategoriesEvents()
    data class AddNewCharacterCategoryCrossRef(val category: Categories) : EditCategoriesEvents()
}