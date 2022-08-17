package com.example.tingxie.presentation.characters

import com.example.tingxie.domain.model.Character
import com.example.tingxie.domain.model.OrderBy

sealed class CharactersEvent {
    data class Delete(val character: Character) : CharactersEvent()
    data class Search(val character: String) : CharactersEvent()
    data class ChangeSorting(val orderBy: OrderBy) : CharactersEvent()
    object ChangeSortingOptionsVisibility : CharactersEvent()
    object RestoreCharacter : CharactersEvent()
}