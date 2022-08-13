package com.example.tingxie.presentation.quiz_statistics.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.tingxie.domain.model.BarChartData
import com.example.tingxie.presentation.edit_character.EditCharacterEvent
import com.example.tingxie.presentation.quiz_statistics.QuizStatisticsViewModel
import com.example.tingxie.presentation.util.BarChart
import com.example.tingxie.presentation.util.CharacterDetail
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
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(maxHeight/2)
                    .align(Alignment.TopStart)
                    .padding(8.dp),
            ){
                BarChart(
                    barChartData = viewModel.state.value.testScoreBarChartData,
                    modifier = Modifier
                        .fillMaxWidth()
                        .matchParentSize(),
                    padding = 8.dp
                )
            }

            Box (
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomStart)
                    .height(maxHeight/2)
                    .padding(8.dp),
            ) {
                LazyColumn {
                    items(viewModel.state.value.quizResults) { char ->
                        CharacterDetail(
                            character = char.character,
                            modifier = Modifier.padding(20.dp),
                            showCharacter = true
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                Column {
                                    Icon(imageVector = Icons.Default.Done, contentDescription = "Correct Answers")
                                    Text(text = char.correctAnswers.toString())
                                }
                                Column {
                                    Icon(imageVector = Icons.Default.Close, contentDescription = "Incorrect answers" )
                                    Text(text = char.incorrectAnswers.toString())

                                }
                            }
                        }
                    }
                }
            }
        }
    }
}