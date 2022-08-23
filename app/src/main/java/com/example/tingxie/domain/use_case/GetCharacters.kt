package com.example.tingxie.domain.use_case

import com.example.tingxie.domain.model.*
import com.example.tingxie.domain.model.util.ChooseCharactersBy
import com.example.tingxie.domain.model.util.OrderCharactersBy
import com.example.tingxie.domain.model.util.Ordering
import com.example.tingxie.domain.repository.CharacterRepository
import com.example.tingxie.domain.use_case.utils.toCharacterStatistics
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*

class GetCharacters (
    private val characterRepository: CharacterRepository
) {
    fun getCharacters(orderBy: OrderCharactersBy = OrderCharactersBy.DateAdded(Ordering.Acsending)): Flow<List<Character>> {
        return characterRepository.getCharacters().map { characters ->
            sortCharacters(characters, orderBy)
        }
    }

    fun getCharactersFromCategoryName(categoryName: String): Flow<List<Character>> {
        return characterRepository.getCharactersFromCategoryName(categoryName)
    }

    suspend fun getCharacter(id: Int): Character? {
        return characterRepository.getCharacter(id)
    }

    fun getCharactersLike(character: String): Flow<List<Character>> {
        return characterRepository.getCharactersLike(character)
    }

    fun getCharactersWithCategoriesLike(searchWord: String): Flow<List<CharacterWithCategories>> {
        return characterRepository.getCharactersWithCategoriesLike(searchWord)
    }

    fun getNRandomCharacters(number: Int): Flow<List<Character>> {
        return characterRepository.getNRandomCharacters(number)
    }

    fun getNRandomCharactersFromCategory(number: Int, categoryName: String): Flow<List<Character>> {
        return characterRepository.getNRandomCharactersFromCategory(categoryName = categoryName, number = number)
    }

    fun getCharactersCategoriesWithId(id: Int): Flow<List<CharacterWithCategories>> {
        return characterRepository.getCharacterWithCategoriesWithId(id)
    }

    fun getCharactersWithCategories(): Flow<List<CharacterWithCategories>> {
        return characterRepository.getCharactersWithCategories()
    }

    @OptIn(FlowPreview::class)
    suspend fun getCharactersBy(chooseCharactersBy: ChooseCharactersBy, categories: Categories? = null): List<Character> {
        if (chooseCharactersBy is ChooseCharactersBy.Random) return getNRandomCharacters(chooseCharactersBy.amount)

        // HERE BE DRAGONS
        // if getCharacterResults is a left join then quizResults maybe null, but if I set quizResult?
        // or List<QuizResults>? room will throw a null pointer exception
        // currently getCharactersResults is an inner join so instead we getCharacters and map them
        // to CharacterStatistics with 0 correct, incorrect, and then getCharactersResults and then
        // add then create a Map<Character, List<CharacterStatistics>?> to order

        val allCharacters = characterRepository.getCharacters()
        val characterResults = characterRepository.getCharacterResults()

        val allCharacterResults: MutableMap<Character, List<CharacterResult>?> = mutableMapOf()
        for (character in allCharacters) {
            if (characterResults.containsKey(character)) {
                allCharacterResults[character] = characterResults[character]
            } else {
                allCharacterResults[character] = null
            }
        }

        val characterStatistics = characterResults.toCharacterStatistics()
        when (chooseCharactersBy) {
            is ChooseCharactersBy.LeastCorrect -> characterStatistics.sortedBy { it.correctAnswers }
            is ChooseCharactersBy.LeastTested -> characterStatistics.sortedBy { it.correctAnswers + it.incorrectAnswers }
            is ChooseCharactersBy.MostIncorrect -> characterStatistics.sortedByDescending { it.incorrectAnswers }
            is ChooseCharactersBy.Random -> {} // Unreachable if it's random we returned early at function start
        }
        return characterStatistics.map { it.character }.take(chooseCharactersBy.amount)
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

        fun sortCharactersWithCategories(characters: List<CharacterWithCategories>, orderBy: OrderCharactersBy): List<CharacterWithCategories> {
            return when (orderBy) {
                // As a western I don't understand how characters are sorted,
                // sorting by pinyin is the behaviour I expect
                is OrderCharactersBy.Character -> {
                    when (orderBy.ordering) {
                        Ordering.Acsending -> characters.sortedBy { it.character.pinyin }
                        Ordering.Descending -> characters.sortedByDescending { it.character.pinyin }
                    }
                }
                // IDs are auto incremented so sorting by date added is the same as sorting by id
                is OrderCharactersBy.DateAdded -> {
                    when (orderBy.ordering) {
                        Ordering.Acsending -> characters.sortedBy { it.character.id }
                        Ordering.Descending -> characters.sortedByDescending { it.character.id }
                    }
                }
                is OrderCharactersBy.CharacterNumber -> {
                    when (orderBy.ordering) {
                        Ordering.Acsending -> characters.sortedBy { it.character.characterNumber }
                        Ordering.Descending -> characters.sortedByDescending { it.character.characterNumber }
                    }
                }
            }
        }
    }
}