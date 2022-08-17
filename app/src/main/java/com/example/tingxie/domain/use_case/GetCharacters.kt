package com.example.tingxie.domain.use_case

import com.example.tingxie.domain.model.Character
import com.example.tingxie.domain.model.OrderBy
import com.example.tingxie.domain.model.Ordering
import com.example.tingxie.domain.repository.CharacterRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetCharacters (
    private val characterRepository: CharacterRepository
) {
    fun getCharacters(orderBy: OrderBy = OrderBy.Id(Ordering.Acsending)): Flow<List<Character>> {
        return characterRepository.getCharacters().map { characters ->
            sortCharacters(characters, orderBy)
        }
    }

    suspend fun getCharacter(id: Int): Character? {
        return characterRepository.getCharacter(id)
    }

    fun getCharactersLike(character: String): Flow<List<Character>> {
        return characterRepository.getCharactersLike(character)
    }

    fun getNRandomCharacters(number: Int): Flow<List<Character>> {
        return characterRepository.getNRandomCharacters(number)
    }

    companion object {
        fun sortCharacters(characters: List<Character>, orderBy: OrderBy): List<Character> {
            return when (orderBy) {
                is OrderBy.Character -> {
                    when (orderBy.ordering) {
                        Ordering.Acsending -> characters.sortedBy { it.character }
                        Ordering.Descending -> characters.sortedByDescending { it.character }
                    }
                }
                is OrderBy.Id -> {
                    when (orderBy.ordering) {
                        Ordering.Acsending -> characters.sortedBy { it.id }
                        Ordering.Descending -> characters.sortedByDescending { it.id }
                    }
                }
            }
        }
    }
}