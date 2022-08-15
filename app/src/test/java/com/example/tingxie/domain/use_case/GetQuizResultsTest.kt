package com.example.tingxie.domain.use_case

import com.example.tingxie.data.repository.EmptyRepository
import com.example.tingxie.data.repository.FakeCharacterRepository
import com.example.tingxie.domain.use_case.utils.testQuizResults
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before

class GetQuizResultsTest {
    private lateinit var getQuizResults: GetQuizResults
    private lateinit var repository: FakeCharacterRepository

    private lateinit var getEmptyQuizResults: GetQuizResults
    val emptyRepository = EmptyRepository()

    @Before
    fun setup() {
        val repository = FakeCharacterRepository()
        val emptyRepository = EmptyRepository()

        val getEmptyQuizResults = GetQuizResults(emptyRepository)
        val getQuizResults = GetQuizResults(repository)

        runBlocking {
            for ((result, char) in testQuizResults) {
                repository.insertQuizResult(result)
                repository.insertCharacter(char)
            }
        }
    }


}