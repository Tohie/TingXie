package com.example.tingxie.domain.use_case

import com.example.tingxie.domain.model.Character
import com.example.tingxie.domain.model.util.ChooseCharactersBy
import com.example.tingxie.domain.model.util.OrderCharacterResultsBy
import com.example.tingxie.domain.model.util.OrderCharactersBy
import com.example.tingxie.domain.model.util.Ordering
import com.example.tingxie.domain.repository.CharacterRepository
import com.example.tingxie.domain.use_case.utils.toCharacterStatistics
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetCharacters (
    private val characterRepository: CharacterRepository
) {
    fun getCharacters(orderBy: OrderCharactersBy = OrderCharactersBy.DateAdded(Ordering.Acsending)): Flow<List<Character>> {
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

    fun getCharactersBy(chooseCharactersBy: ChooseCharactersBy): Flow<List<Character>> {
        if (chooseCharactersBy is ChooseCharactersBy.Random) return getNRandomCharacters(chooseCharactersBy.amount)

        return characterRepository.getCharacterResults().map { characterResults ->
            // These transformations could be done by SQL but this method is easier and there's only
            // a max of 20 results so I think the time difference will be very small
            val characterStatistics = characterResults.toCharacterStatistics()
            when (chooseCharactersBy) {
                is ChooseCharactersBy.LeastCorrect -> characterStatistics.sortedBy { it.correctAnswers }
                is ChooseCharactersBy.LeastTested -> characterStatistics.sortedBy { it.correctAnswers + it.incorrectAnswers }
                is ChooseCharactersBy.MostIncorrect -> characterStatistics.sortedByDescending { it.incorrectAnswers }
                is ChooseCharactersBy.Random -> {} // Unreachable if it's random we returned early at function start
            }
            characterStatistics.map { it.character }.take(chooseCharactersBy.amount)
        }
    }

    companion object {
        fun sortCharacters(characters: List<Character>, orderBy: OrderCharactersBy): List<Character> {
            return when (orderBy) {
                // As a western I don't understand how characters are sorted,
                // sorting by pinyin is the behaviour I expect
                is OrderCharactersBy.Character -> {
                    when (orderBy.ordering) {
                        Ordering.Acsending -> characters.sortedBy { it.pinyin }
                        Ordering.Descending -> characters.sortedByDescending { it.pinyin }
                    }
                }
                // IDs are auto incremented so sorting by date added is the same as sorting by id
                is OrderCharactersBy.DateAdded -> {
                    when (orderBy.ordering) {
                        Ordering.Acsending -> characters.sortedBy { it.id }
                        Ordering.Descending -> characters.sortedByDescending { it.id }
                    }
                }
                is OrderCharactersBy.CharacterNumber -> {
                    when (orderBy.ordering) {
                        Ordering.Acsending -> characters.sortedBy { it.characterNumber }
                        Ordering.Descending -> characters.sortedByDescending { it.characterNumber }
                    }
                }
            }
        }
    }
}