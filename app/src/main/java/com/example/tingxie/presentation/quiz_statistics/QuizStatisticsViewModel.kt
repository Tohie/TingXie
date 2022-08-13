package com.example.tingxie.presentation.quiz_statistics

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tingxie.domain.model.Character
import com.example.tingxie.domain.use_case.CharacterUseCases
import com.example.tingxie.presentation.character_quiz.CharacterQuizState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class QuizStatisticsViewModel @Inject constructor(
    private val characterUseCases: CharacterUseCases
) : ViewModel() {
    private var _state = mutableStateOf<QuizStatisticsState>(QuizStatisticsState())
    val state: State<QuizStatisticsState> = _state

    init {
        getCharacterQuizResults()
    }

    private fun getCharacterQuizResults() {
        characterUseCases.getQuizResults().onEach { quizResults ->
            val sortedByCharacter = quizResults.toList().sortedBy { (_, value) ->
                value.id
            }.toMap()

            val characterMap: MutableMap<Character, CharacterQuizStatistic> = mutableMapOf()
            for ((quizResult, character) in sortedByCharacter) {
                if (characterMap.containsKey(character) && quizResult.isCorrect) {
                    characterMap[character] = CharacterQuizStatistic(
                        correctAnswers = characterMap[character]!!.correctAnswers + 1,
                        incorrectAnswers = characterMap[character]!!.incorrectAnswers,
                        character = character
                    )
                } else if (!characterMap.containsKey(character)){
                    characterMap[character] = CharacterQuizStatistic(
                        character = character,
                        correctAnswers = if (quizResult.isCorrect) 1 else 0,
                        incorrectAnswers = 0
                    )
                }
            }
            _state.value = _state.value.copy(quizResults = characterMap.values.toList())
        }.launchIn(viewModelScope)
    }
}