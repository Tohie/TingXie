package com.example.tingxie.presentation.characters

import com.example.tingxie.domain.model.Character

data class CharactersState(
    val characters: List<Character> = emptyList()
)