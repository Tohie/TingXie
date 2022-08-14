package com.example.tingxie.presentation.quiz_statistics

sealed class QuizStatisticsEvent() {
    data class DateChanged(val year: Int, val month: Int, val dayOfMonth: Int) : QuizStatisticsEvent()
    data class ChangeNumberOfTestsDisplayed(val amount: Int) : QuizStatisticsEvent()
}
