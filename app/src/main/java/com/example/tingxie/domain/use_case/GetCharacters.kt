package com.example.tingxie.domain.use_case

import com.example.tingxie.domain.model.Character
import com.example.tingxie.domain.repository.CharacterRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCharacters (
    private val characterRepository: CharacterRepository
) {
    operator fun invoke(): Flow<List<Character>> {
        // Can do ordering here
        return characterRepository.getCharacters()
    }
}