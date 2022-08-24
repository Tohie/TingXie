package com.example.tingxie.presentation.character_quiz_results.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.tingxie.domain.model.CharacterStatistics
import com.example.tingxie.presentation.character_quiz_results.CharacterQuizResultsViewModel
import com.example.tingxie.presentation.util.BottomBar
import com.example.tingxie.presentation.util.CharacterDetail
import com.example.tingxie.presentation.util.Screen
import com.example.tingxie.presentation.util.TopBar

@Composable
fun CharacterQuizResultsScreen(
    navController: NavController,
    viewModel: CharacterQuizResultsViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = { TopBar {} },
        bottomBar = { BottomBar(navController = navController)}
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {

                CongratulationsMessage(viewModel)

                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(
                        onClick = {
                            navController.navigate(Screen.CharacterQuizScreen.route)
                        }
                    ) {
                        Text(text = "Take another quiz")
                    }
                }
                
                QuizResultBreakdown(viewModel)


            }
        }
    }
}

@Composable
private fun CongratulationsMessage(viewModel: CharacterQuizResultsViewModel) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Congrats!!!")
        Text(text = "You got ${viewModel.state.value.quizResults.score} out of ${viewModel.state.value.quizResults.numberOfCharacters}")
        Text(text = "Below is a detailed breakdown of which character you got right and wrong!")
    }
}

@Composable
private fun QuizResultBreakdown(viewModel: CharacterQuizResultsViewModel) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth()
    ) {
        items(
            items = viewModel.state.value.characterStatistics,
            key = { item: CharacterStatistics -> item.character.id!! }
        ) { characterResult ->
            CharacterDetail(
                character = characterResult.character,
                showCharacter = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                Categories = {}
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (characterResult.correctAnswers == 1) {
                        Icon(imageVector = Icons.Default.Done, contentDescription = "Correct")
                    } else {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Incorrect")
                    }
                }
            }
        }
    }
}