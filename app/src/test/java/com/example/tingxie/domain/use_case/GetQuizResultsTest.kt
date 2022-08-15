package com.example.tingxie.domain.use_case

import com.example.tingxie.data.repository.EmptyRepository
import com.example.tingxie.data.repository.FakeCharacterRepository
import com.example.tingxie.domain.model.CharacterQuizStatistics
import com.example.tingxie.domain.use_case.utils.expectedBarChartResults
import com.example.tingxie.domain.use_case.utils.expectedCharacterResults
import com.example.tingxie.domain.use_case.utils.testQuizResults
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

}