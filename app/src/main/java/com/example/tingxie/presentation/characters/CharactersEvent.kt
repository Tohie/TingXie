package com.example.tingxie.presentation.characters

import com.example.tingxie.domain.model.Categories
import com.example.tingxie.domain.model.CharacterWithCategories
import com.example.tingxie.domain.model.util.OrderCharactersBy

sealed class CharactersEvent {
    data class Delete(val character: CharacterWithCategories) : CharactersEvent()
    data class Search(val character: String) : CharactersEvent()
    data class ChangeSorting(val orderBy: OrderCharactersBy) : CharactersEvent()
    data class ChangeAmountOfCharactersToTest(val amount: Float) : CharactersEvent()
    data class ChangeCategories(val category: Categories?) : CharactersEvent()
    object ChangeSortingOptionsVisibility : CharactersEvent()
    object RestoreCharacter : CharactersEvent()
}