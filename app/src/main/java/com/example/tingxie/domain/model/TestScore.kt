package com.example.tingxie.domain.model

class TestScore(
    var currentScore: Int = 0,
    var totalScore: Int = 0
) {
    fun addToCurrentScore(i: Int) {
        currentScore += i
    }

    fun addToTotalScore(i: Int) {
        totalScore += i
    }
}