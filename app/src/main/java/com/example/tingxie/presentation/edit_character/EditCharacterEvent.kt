package com.example.tingxie.presentation.edit_character

import androidx.compose.ui.focus.FocusState
import com.example.tingxie.domain.model.Categories
import com.example.tingxie.domain.model.Character
import com.example.tingxie.domain.model.CharacterCategoryCrossRef

sealed class EditCharacterEvent {
    data class EnteredCharacterNumber(val value: String): EditCharacterEvent()
    data class EnteredCharacter(val value: String): EditCharacterEvent()
    data class EnteredPinyin(val value: String): EditCharacterEvent()
    data class EnteredDescription(val value: String): EditCharacterEvent()
    data class EnteredCategoryName(val value: String): EditCharacterEvent()

    object SaveCharacter : EditCharacterEvent()
    object SaveCategory : EditCharacterEvent()
}
