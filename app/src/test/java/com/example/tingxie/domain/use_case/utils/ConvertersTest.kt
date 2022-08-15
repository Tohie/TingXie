package com.example.tingxie.domain.use_case.utils

import androidx.compose.ui.graphics.Color
import com.example.tingxie.domain.model.BarChartData
import com.example.tingxie.domain.model.Character
import com.example.tingxie.domain.model.CharacterQuizStatistic
import com.example.tingxie.domain.model.QuizResult
import org.junit.Assert.*
import org.junit.Test
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import com.google.common.truth.Truth.assertThat


class ConvertersTest {

    @Test
    fun `convertToTestScoreWorks as expected`() {
        val actualBarChartResults = Converters.convertQuizResultsToTestData(testQuizResults)
        assertThat(actualBarChartResults).isEqualTo(expectedBarChartResults)
    }

    @Test
    fun `convertToQuizStatisticsWorks as expected`() {
        val actualCharacterResults = Converters.convertQuizResultsToList(testQuizResults)
        assertThat(actualCharacterResults).isEqualTo(expectedCharacterResults)
    }

    @Test
    fun `convertToTestScoreWorks works with empty data, doesn't crash`() {
        val actualBarChartResults = Converters.convertQuizResultsToTestData(mapOf())
        assertThat(actualBarChartResults).isEqualTo(listOf<BarChartData>())
    }

    @Test
    fun `convertToList works with empty data, doesn't crash`() {
        val actualCharacterResults = Converters.convertQuizResultsToList(mapOf())
        assertThat(actualCharacterResults).isEqualTo(listOf<CharacterQuizStatistic>())
    }
}