package com.example.tingxie.domain.use_case.utils

import androidx.compose.ui.graphics.Color
import com.example.tingxie.domain.model.*
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun Map<QuizResult, Character>.toTestResultData(): List<CharacterQuizBarChartData> {
    val testScores: MutableMap<Long, SingleTestScores> = mutableMapOf()

    for ((quizResult, _) in this) {
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

fun Map<Character, List<QuizResult>>.toCharacterQuizStatistics(): List<CharacterQuizStatistics> {
    return this.map { (char, quizResults) ->
        val (correct, incorrect) = quizResults.foldRight(Pair(0, 0)) { character, (correct, incorrect) ->
            when (character.isCorrect) {
                true -> Pair(correct + 1, incorrect)
                false -> Pair(correct, incorrect + 1)
            }
        }
        CharacterQuizStatistics(
            character = char,
            incorrectAnswers = incorrect,
            correctAnswers = correct
        )
    }
}

fun Map<QuizResult, Character>.toQuizResultList(): List<QuizResults> {
    return this.map { (result, char) ->
        QuizResults(
            character = char,
            wasCorrect = result.isCorrect
        )
    }
}