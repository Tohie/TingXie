package com.example.tingxie.domain.use_case

import com.example.tingxie.domain.model.Character
import com.example.tingxie.domain.model.InvalidCharacterException
import com.example.tingxie.domain.repository.CharacterRepository

class AddCharacter (
    private val repository: CharacterRepository
){
    @Throws(InvalidCharacterException::class)
    suspend operator fun invoke(character: Character) {
        // Can include validation here
        if (character.character.isBlank()) {
            throw InvalidCharacterException("Character can't be blank")
        }
        if (character.pinyin.isBlank()) {
            throw InvalidCharacterException("Pinyin can't be blank")
        }
        if (character.description.isBlank()) {
            throw InvalidCharacterException("Description can't be blank")
        }
        repository.insertCharacter(character)
    }
}