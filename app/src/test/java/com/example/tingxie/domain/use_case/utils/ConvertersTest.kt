package com.example.tingxie.domain.use_case.utils

import com.example.tingxie.domain.model.Character
import com.example.tingxie.domain.model.CharacterQuizBarChartData
import com.example.tingxie.domain.model.CharacterQuizStatistics
import com.example.tingxie.domain.model.QuizResult
import org.junit.Test
import com.google.common.truth.Truth.assertThat


class ConvertersTest {

    @Test
    fun `convertToTestScoreWorks as expected`() {
        val actualBarChartResults = testQuizResults.toTestResultData()
        assertThat(actualBarChartResults).isEqualTo(expectedBarChartResults)
    }

    @Test
    fun `convertToQuizStatisticsWorks as expected`() {
        val actualCharacterResults = testQuizResults.toCharacterQuizStatistics()
        assertThat(actualCharacterResults).isEqualTo(expectedCharacterResults)
    }

    @Test
    fun `convertToTestScoreWorks works with empty data, doesn't crash`() {
        val actualBarChartResults = mapOf<QuizResult, Character>().toTestResultData()
        assertThat(actualBarChartResults).isEqualTo(listOf<CharacterQuizBarChartData>())
    }

    @Test
    fun `convertToList works with empty data, doesn't crash`() {
        val actualCharacterResults = mapOf<QuizResult, Character>().toCharacterQuizStatistics()
        assertThat(actualCharacterResults).isEqualTo(listOf<CharacterQuizStatistics>())
    }
}