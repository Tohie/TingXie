package com.example.tingxie.domain.use_case

import com.example.tingxie.domain.model.BarChartData
import com.example.tingxie.domain.model.Character
import com.example.tingxie.domain.model.QuizResult
import com.example.tingxie.domain.repository.CharacterRepository
import com.example.tingxie.domain.use_case.utils.Converters
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.util.*

class GetQuizResultsBetween(
    private val repository: CharacterRepository
) {
    operator fun invoke(year: Int, month: Int, dayOfMonth: Int): Flow<List<BarChartData>> {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)
        val time = calendar.time
        val localDate = time.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
        val startOfDay = localDate.atStartOfDay().toInstant(ZoneOffset.MIN).toEpochMilli()
        val endOfDay = localDate.atTime(LocalTime.MAX).toInstant(ZoneOffset.MAX).toEpochMilli()

        return repository.getQuizResultBetween(startOfDay, endOfDay).map {
            Converters.convertQuizResultsToTestData(it)
        }
    }
}