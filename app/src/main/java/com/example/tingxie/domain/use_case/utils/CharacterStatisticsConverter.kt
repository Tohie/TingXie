package com.example.tingxie.domain.use_case.utils

import com.example.tingxie.domain.model.Character
import com.example.tingxie.domain.model.CharacterResult
import com.example.tingxie.domain.model.CharacterStatistics

fun Map<Character, List<CharacterResult?>>.toCharacterStatistics(): List<CharacterStatistics> {
    return this.map { (char, result: List<CharacterResult?>) ->

        result.foldRight(
            CharacterStatistics(
                character = char,
                correctAnswers = 0,
                incorrectAnswers = 0,
            )
        ) { characterResult, characterStatistic: CharacterStatistics ->
            when (characterResult) {
                null -> characterStatistic
                else -> {
                    val correctAnswers = if (characterResult.quizResult.isCorrect) {
                        characterStatistic.correctAnswers + 1
                    } else {
                        characterStatistic.correctAnswers
                    }

                    val incorrectAnswer = if (characterResult.quizResult.isCorrect) {
                        characterStatistic.incorrectAnswers
                    } else {
                        characterStatistic.incorrectAnswers + 1
                    }

                    CharacterStatistics(
                        character = char,
                        correctAnswers = correctAnswers,
                        incorrectAnswers = incorrectAnswer
                    )
                }
            }
        }
    }
}