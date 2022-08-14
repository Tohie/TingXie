package com.example.tingxie.presentation.quiz_statistics.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.tingxie.presentation.quiz_statistics.QuizStatisticsViewModel
import com.example.tingxie.presentation.util.TopBar

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
        if (viewModel.state.value.quizResults.isEmpty() || viewModel.state.value.testScoreBarChartData.isEmpty()) {
            return@Scaffold
        }

        BoxWithConstraints(
            modifier = Modifier.fillMaxSize()
        ) {
            QuizStatisticsGraph(
                viewModel = viewModel,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(maxHeight / 2)
                    .align(Alignment.TopStart)
                    .padding(8.dp),
            )

            QuizStatisticsCharacterList (
                viewModel = viewModel,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomStart)
                    .height(this@BoxWithConstraints.maxHeight / 2)
                    .padding(8.dp),
            )
        }
    }
}