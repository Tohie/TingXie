package com.example.tingxie.presentation.quiz_statistics.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.tingxie.presentation.quiz_statistics.QuizStatisticsViewModel
import com.example.tingxie.presentation.util.CharacterDetail

@Composable
fun QuizStatisticsCharacterList(
    viewModel: QuizStatisticsViewModel,
    modifier: Modifier = Modifier
) {
    Box (
        modifier = modifier
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
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
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