package com.example.tingxie.domain.use_case.utils

import androidx.compose.ui.graphics.Color
import com.example.tingxie.domain.model.CharacterQuizBarChartData
import com.example.tingxie.domain.model.Character
import com.example.tingxie.domain.model.CharacterQuizStatistics
import com.example.tingxie.domain.model.QuizResult
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

val october15Calendar = GregorianCalendar(2022, 8, 15)
val october14Calendar = GregorianCalendar(2022, 8, 14)

val october15Instant = october15Calendar.toInstant().atZone(ZoneId.systemDefault())
val october14Instant = october14Calendar.toInstant().atZone(ZoneId.systemDefault())

val october15TimeStamp = october15Instant.toInstant().toEpochMilli()
val october14Timestamp = october14Instant.toInstant().toEpochMilli()

val testQuizResults: Map<QuizResult, Character> = mapOf(
    Pair(
        QuizResult(
            resultId = 0,
            timestamp = october14Timestamp,
            isCorrect = true,
            characterIdMap = 0
        ),
        Character(
            id = 0,
            character =  "你",
            pinyin = "ni",
            description = "you"
        )
    ),
    Pair(
        QuizResult(
            resultId = 1,
            timestamp = october14Timestamp,
            isCorrect = false,
            characterIdMap = 0
        ),
        Character(
            id = 0,
            character =  "你",
            pinyin = "ni",
            description = "you"
        )
    ),
    Pair(
        QuizResult(
            resultId = 2,
            timestamp = october15TimeStamp,
            isCorrect = true,
            characterIdMap = 1
        ),
        Character(
            id = 1,
            character =  "b",
            pinyin = "ni",
            description = "you"
        )
    ),
    Pair(
        QuizResult(
            resultId = 3,
            timestamp = october15TimeStamp,
            isCorrect = true,
            characterIdMap = 1
        ),
        Character(
            id = 1,
            character =  "b",
            pinyin = "ni",
            description = "you"
        )
    )
)

val formatter = DateTimeFormatter.ofPattern("dd/MM/yy hh:ss")
    .withZone(ZoneId.systemDefault())
val expectedDateTest1 = formatter.format(Instant.ofEpochMilli(october14Timestamp))
val expectedDateTest2 = formatter.format(Instant.ofEpochMilli(october15TimeStamp))

val expectedBarChartResults: List<CharacterQuizBarChartData> = listOf(
    CharacterQuizBarChartData(
        label = expectedDateTest1,
        color = Color(255, 210, 117),
        value = 50f
    ),
    CharacterQuizBarChartData(
        label = expectedDateTest2,
        color = Color(255, 210, 117),
        value = 100f
    )
)

val expectedCharacterResults: List<CharacterQuizStatistics> = listOf(
    CharacterQuizStatistics(
        character = Character(
            id = 0,
            character =  "你",
            pinyin = "ni",
            description = "you"
        ),
        correctAnswers = 1,
        incorrectAnswers = 1
    ),
    CharacterQuizStatistics(
        character = Character(
            id = 1,
            character =  "b",
            pinyin = "ni",
            description = "you"
        ),
        correctAnswers = 2,
        incorrectAnswers = 0
    )
)
