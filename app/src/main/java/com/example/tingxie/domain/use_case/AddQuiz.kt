package com.example.tingxie.domain.use_case

import com.example.tingxie.domain.model.Quiz
import com.example.tingxie.domain.repository.CharacterRepository

class AddQuiz(
    private val repository: CharacterRepository
) {
    suspend operator fun invoke(quiz: Quiz): Long {
        return repository.insertQuiz(quiz)
    }
}