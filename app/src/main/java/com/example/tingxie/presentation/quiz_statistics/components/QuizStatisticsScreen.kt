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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.tingxie.presentation.edit_character.EditCharacterEvent
import com.example.tingxie.presentation.quiz_statistics.QuizStatisticsViewModel
import com.example.tingxie.presentation.util.CharacterDetail
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
            items(viewModel.state.value.quizResults) { char ->
                CharacterDetail(
                    character = char.character,
                    modifier = Modifier.padding(8.dp),
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