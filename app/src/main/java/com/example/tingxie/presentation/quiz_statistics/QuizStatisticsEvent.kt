package com.example.tingxie.presentation.quiz_statistics

import com.example.tingxie.domain.model.util.OrderCharacterResultsBy

sealed class QuizStatisticsEvent {
    data class DateChanged(val year: Int, val month: Int, val dayOfMonth: Int) : QuizStatisticsEvent()
    data class ChangeNumberOfTestsDisplayed(val amount: Int) : QuizStatisticsEvent()
    data class Search(val searchQuery: String) : QuizStatisticsEvent()

    object ChangeSortingOptionsVisibility : QuizStatisticsEvent()
    data class OrderResultsBy(val ordering: OrderCharacterResultsBy) : QuizStatisticsEvent()
}
