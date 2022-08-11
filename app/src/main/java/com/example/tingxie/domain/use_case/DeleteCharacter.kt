package com.example.tingxie.domain.use_case

import com.example.tingxie.domain.model.Character
import com.example.tingxie.domain.repository.CharacterRepository

class DeleteCharacter (
    private val repository: CharacterRepository
){
    suspend operator fun invoke(character: Character) {
        // Can check character was deleted/validation here
        repository.deleteCharacter(character)
    }
}