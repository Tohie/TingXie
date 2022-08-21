package com.example.tingxie.presentation.edit_character

import androidx.compose.ui.focus.FocusState
import com.example.tingxie.domain.model.Character

sealed class EditCharacterEvent {
    data class EnteredCharacterNumber(val value: String): EditCharacterEvent()
    data class ChangeCharacterNumberFocus(val focusState: FocusState): EditCharacterEvent()

    data class EnteredCharacter(val value: String): EditCharacterEvent()
    data class ChangeCharacterFocus(val focusState: FocusState): EditCharacterEvent()

    data class EnteredPinyin(val value: String): EditCharacterEvent()
    data class ChangePinyinFocus(val focusState: FocusState): EditCharacterEvent()

    data class EnteredDescription(val value: String): EditCharacterEvent()
    data class ChangeDescriptionFocus(val focusState: FocusState): EditCharacterEvent()

    data class EnteredCategoryName(val value: String): EditCharacterEvent()
    data class ChangeCategoryNameFocus(val focusState: FocusState): EditCharacterEvent()

    object SaveCharacter : EditCharacterEvent()
    object SaveCategory : EditCharacterEvent()
}
