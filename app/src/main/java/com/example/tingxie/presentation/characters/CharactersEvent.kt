package com.example.tingxie.presentation.characters

import com.example.tingxie.domain.model.Character

sealed class CharactersEvent {
    data class Delete(val character: Character) : CharactersEvent()
    object RestoreCharacter : CharactersEvent()
}