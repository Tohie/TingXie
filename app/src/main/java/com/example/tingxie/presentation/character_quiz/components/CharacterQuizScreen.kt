package com.example.tingxie.presentation.character_quiz.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.tingxie.R
import com.example.tingxie.presentation.character_quiz.CharacterQuizEvents
import com.example.tingxie.presentation.character_quiz.CharactersQuizViewModel
import com.example.tingxie.presentation.edit_character.EditCharacterViewModel
import com.example.tingxie.presentation.util.Screen
import kotlinx.coroutines.flow.collectLatest

@Composable
fun CharacterQuizScreen(
    navController: NavController,
    viewModel: CharactersQuizViewModel = hiltViewModel()
) {
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is CharactersQuizViewModel.UiEvent.QuizFinished -> {
                    scaffoldState.snackbarHostState.showSnackbar(event.finalMessage)
                    navController.navigate(Screen.CharactersScreen.route)
                }
            }
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        bottomBar = {
            BottomAppBar {
                Row(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    IconButton(onClick = {
                        viewModel.onEvent(CharacterQuizEvents.NextCharacter(wasCorrect = false))
                    }) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Incorrect")
                    }

                    IconButton(onClick = {
                        viewModel.onEvent(CharacterQuizEvents.NextCharacter(wasCorrect = true))
                    }) {
                        Icon(imageVector = Icons.Default.Done, contentDescription = "Correct")
                    }

                    IconButton(onClick = {
                        viewModel.onEvent(CharacterQuizEvents.ChangeCharacterVisibility(!viewModel.state.value.isCharacterVisible))
                    }) {
                        Icon(
                            imageVector = if (viewModel.state.value.isCharacterVisible) {
                                Icons.Default.Visibility
                            } else {
                                Icons.Default.VisibilityOff
                            },
                            contentDescription = "Show/hide"
                        )
                    }
                }
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (viewModel.state.value.currentCharacter != null) {
                val currentCharacter = viewModel.state.value.currentCharacter!!
                if (viewModel.state.value.isCharacterVisible) {
                    Text(
                        text = currentCharacter.character,
                        style = MaterialTheme.typography.body1,
                        color = MaterialTheme.colors.onSurface,
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                }
                Text(
                    text = currentCharacter.pinyin,
                    style = MaterialTheme.typography.body1,
                    color = MaterialTheme.colors.onSurface,
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = currentCharacter.description,
                    style = MaterialTheme.typography.body1,
                    color = MaterialTheme.colors.onSurface,
                )
                Spacer(modifier = Modifier.height(8.dp))


                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }