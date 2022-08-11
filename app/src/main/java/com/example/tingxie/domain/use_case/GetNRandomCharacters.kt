package com.example.tingxie.domain.use_case

import com.example.tingxie.domain.model.Character
import com.example.tingxie.domain.repository.CharacterRepository
import kotlinx.coroutines.flow.Flow

class GetNRandomCharacters(
    private val repository: CharacterRepository
) {
    operator fun invoke(number: Int): Flow<List<Character>> {
        return repository.getNRandomCharacters(number)
    }
}