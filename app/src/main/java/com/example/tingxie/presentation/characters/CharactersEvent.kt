package com.example.tingxie.presentation.characters

import com.example.tingxie.domain.model.Character
import com.example.tingxie.domain.model.util.OrderCharactersBy

sealed class CharactersEvent {
    data class Delete(val character: Character) : CharactersEvent()
    data class Search(val character: String) : CharactersEvent()
    data class ChangeSorting(val orderBy: OrderCharactersBy) : CharactersEvent()
    data class ChangeAmountOfCharactersToTest(val amount: Float) : CharactersEvent()
    object ChangeSortingOptionsVisibility : CharactersEvent()
    object RestoreCharacter : CharactersEvent()
}