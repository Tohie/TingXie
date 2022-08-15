package com.example.tingxie.domain.use_case.utils

import androidx.compose.ui.graphics.Color
import com.example.tingxie.domain.model.*
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

object Converters {
    fun convertQuizResultsToTestData(quizResults: Map<QuizResult, Character>): List<CharacterQuizBarChartData> {
        val testScores: MutableMap<Long, SingleTestScores> = mutableMapOf()
        for ((quizResult, _) in quizResults) {
            if (testScores.containsKey(quizResult.timestamp)) {
                testScores[quizResult.timestamp]!!.addToTotalScore(1)
                if (quizResult.isCorrect) {
                    testScores[quizResult.timestamp]!!.addToCurrentScore(1)
                }
            } else {
                testScores[quizResult.timestamp] = SingleTestScores(
                    currentScore = if (quizResult.isCorrect) 1 else 0,
                    totalScore = 1
                )
            }
        }

        val testScoreBarChartData = mutableListOf<CharacterQuizBarChartData>()
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yy hh:ss")
            .withZone(ZoneId.systemDefault())

        for ((timestamp, testScore) in testScores) {
            val date = formatter.format(Instant.ofEpochMilli(timestamp))

            val newBarChartData = CharacterQuizBarChartData(
                label = date!!,
                value = (testScore.currentScore.toFloat() / testScore.totalScore.toFloat())*100,
                color = Color(255, 210, 117)
            )
            testScoreBarChartData.add(newBarChartData)
        }
        return testScoreBarChartData
    }

    fun convertQuizResultsToList(quizResults: Map<QuizResult, Character>): List<CharacterQuizStatistics> {
        val characterMap: MutableMap<Character, CharacterQuizStatistics> = mutableMapOf()
        for ((quizResult, character) in quizResults) {
            if (characterMap.containsKey(character)) {
                val currentCorrectAnswers = characterMap[character]!!.correctAnswers
                val currentIncorrectAnswers = characterMap[character]!!.incorrectAnswers
                characterMap[character] = CharacterQuizStatistics(
                    correctAnswers = if (quizResult.isCorrect) currentCorrectAnswers + 1 else currentCorrectAnswers,
                    incorrectAnswers = if (!quizResult.isCorrect) currentIncorrectAnswers + 1 else currentIncorrectAnswers,
                    character = character
                )
            } else if (!characterMap.containsKey(character)){
                characterMap[character] = CharacterQuizStatistics(
                    character = character,
                    correctAnswers = if (quizResult.isCorrect) 1 else 0,
                    incorrectAnswers = if (!quizResult.isCorrect) 1 else 0
                )
            }
        }
        return characterMap.values.toList()
    }
}