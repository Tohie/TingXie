package com.example.tingxie.domain.use_case

import androidx.compose.ui.graphics.Color
import com.example.tingxie.data.repository.EmptyRepository
import com.example.tingxie.data.repository.FakeCharacterRepository
import com.example.tingxie.domain.model.CharacterQuizBarChartData
import com.example.tingxie.domain.model.CharacterQuizStatistics
import com.example.tingxie.domain.use_case.utils.expectedBarChartResults
import com.example.tingxie.domain.use_case.utils.expectedCharacterResults
import com.example.tingxie.domain.use_case.utils.testQuizResults
import com.example.tingxie.presentation.util.StatisticsBarChart
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GetQuizResultsTest {
    private lateinit var getQuizResults: GetQuizResults
    private lateinit var repository: FakeCharacterRepository

    private lateinit var getEmptyQuizResults: GetQuizResults
    val emptyRepository = EmptyRepository()

    @Before
    fun setup() {
        val repository = FakeCharacterRepository()
        val emptyRepository = EmptyRepository()

        getEmptyQuizResults = GetQuizResults(emptyRepository)
        getQuizResults = GetQuizResults(repository)

        runBlocking {
            for ((result, char) in testQuizResults) {
                repository.insertQuizResult(result)
                repository.insertCharacter(char)
            }
        }
    }

    @Test
    fun `getCharacterResults sorts results into characters, returns expected statistics`() {
        runBlocking {
            getQuizResults.getCharacterQuizResults().onEach { quizStatistics ->
                assertThat(quizStatistics).isEqualTo(expectedCharacterResults)
            }
        }
    }

    @Test
    fun `getCharacterResults doesn't break on empty input, returns empty list`() {
        runBlocking {
            getEmptyQuizResults.getCharacterQuizResults().onEach { quizStatistics ->
                assertThat(quizStatistics).isEqualTo(listOf<CharacterQuizStatistics>())
            }
        }
    }

    @Test
    fun `getQuizResults sorts results into quizzes, returns expected quizzes`() {
        runBlocking {
            getQuizResults.getTestScoreData().onEach { testData ->
                assertThat(testData).isEqualTo(expectedBarChartResults)
            }
        }
    }

    @Test
    fun `getQuizResults doesn't break on empty input, returns empty list`() {
        runBlocking {
            getEmptyQuizResults.getTestScoreData().onEach { testData ->
                assertThat(testData).isEqualTo(listOf<CharacterQuizStatistics>())
            }
        }
    }

    @Test
    fun `getQuizResultsOn returns quizzes on a specified day`() {
        runBlocking {
            getQuizResults.getQuizResultsOn(2022, 8, 14).onEach { testQuizResults ->
                assertThat(testQuizResults).hasSize(1)
                assertThat(testQuizResults).isEqualTo(listOf(
                    CharacterQuizBarChartData(
                        label = "2022/8/14 00:00",
                        value = 50f,
                        color = Color(255, 210, 117)
                    )
                ))
            }

            getQuizResults.getQuizResultsOn(2022, 8, 15).onEach { testQuizResults ->
                assertThat(testQuizResults).hasSize(1)
            }

            getQuizResults.getQuizResultsOn(2022, 8, 4).onEach { testQuizResults ->
                assertThat(testQuizResults).hasSize(0)
            }
        }
    }

    @Test
    fun `getQuizResultsLimitedBy returns correct amount of quizzes in descending order from most recent`() {
        runBlocking {
            getQuizResults.getQuizResultsLimitedBy(1).onEach { result ->
                assertThat(result).hasSize(1)
                assertThat(result).isEqualTo(listOf(
                    CharacterQuizBarChartData(
                        label = "2022/8/15 00:00",
                        value = 100f,
                        color = Color(255, 210, 117)
                    )
                ))
            }

            // Test data only has two tests, so when user asks for more should return all results
            getQuizResults.getQuizResultsLimitedBy(100).onEach { result ->
                assertThat(result).hasSize(2)
            }
        }
    }

    @Test
    fun `convertingToTimeStamp works`() {
        val (start, end) = GetQuizResults.timestampToStartAndEndOfDay(2022,8, 16)
        assertThat(start).isLessThan(end)
        assertThat(start).isEqualTo(1663257600000)
        assertThat(end).isEqualTo(1663343999999)

    }
}