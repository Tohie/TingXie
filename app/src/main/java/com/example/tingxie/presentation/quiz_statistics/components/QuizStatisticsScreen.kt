package com.example.tingxie.presentation.quiz_statistics.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.tingxie.presentation.edit_character.EditCharacterEvent
import com.example.tingxie.presentation.quiz_statistics.QuizStatisticsViewModel
import com.example.tingxie.presentation.util.TopBar
import java.time.Instant

@Composable
fun QuizStatisticsScreen(
    viewModel: QuizStatisticsViewModel = hiltViewModel(),
    navController: NavController
) {
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = { TopBar() },
        scaffoldState = scaffoldState
    ) {
        LazyColumn {
            items(viewModel.state.value.quizResults.toList()) { (quizResult, char) ->
                Text(text = char.character)
                Text(text = quizResult.isCorrect.toString())
                Text(text = "Quiz Time ${quizResult.timestamp}")
            }
        }
    }
}