package com.example.tingxie.domain.use_case.utils

import com.example.tingxie.domain.model.CharacterQuizBarChartData
import com.example.tingxie.domain.model.CharacterQuizStatistics
import org.junit.Test
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
        assertThat(actualBarChartResults).isEqualTo(listOf<CharacterQuizBarChartData>())
    }

    @Test
    fun `convertToList works with empty data, doesn't crash`() {
        val actualCharacterResults = Converters.convertQuizResultsToList(mapOf())
        assertThat(actualCharacterResults).isEqualTo(listOf<CharacterQuizStatistics>())
    }
}