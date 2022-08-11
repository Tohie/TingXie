package com.example.tingxie.presentation.characters.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.tingxie.presentation.characters.CharactersEvent
import com.example.tingxie.presentation.characters.CharactersViewModel
import com.example.tingxie.presentation.util.CharacterDetail
import com.example.tingxie.presentation.util.Screen
import com.example.tingxie.presentation.util.TopBar
import kotlinx.coroutines.launch

@Composable
fun CharactersScreen(
    navController: NavController,
    viewModel: CharactersViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = { TopBar() },
        bottomBar = {
            BottomAppBar() {
                Row(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    IconButton(
                        onClick = {
                            navController.navigate(Screen.EditCharacterScreen.route)
                        },
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "Add new note")
                    }
                    IconButton(
                        onClick = {
                            navController.navigate(Screen.CharacterQuizScreen.route + "?characterNumber=5")
                        }
                    ) {
                        Icon(imageVector = Icons.Default.Checklist, contentDescription = "Start a test")
                    }
                }
            }
        },
        scaffoldState = scaffoldState
    ) {
        LazyColumn (
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            items(state.characters) { character ->
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CharacterDetail(
                        character = character,
                        modifier = Modifier
                            .clickable {
                                navController.navigate(
                                    Screen.EditCharacterScreen.route + "?characterId=${character.id}"
                                )
                            }
                            .padding(8.dp),
                        showCharacter = true
                    ) {
                        Row (
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            IconButton(
                                onClick = {
                                    viewModel.onEvent(CharactersEvent.Delete(character))
                                    scope.launch {
                                        val result = scaffoldState.snackbarHostState.showSnackbar(
                                            message = "Note deleted",
                                            actionLabel = "Undo"
                                        )
                                        if (result == SnackbarResult.ActionPerformed) {
                                            viewModel.onEvent(CharactersEvent.RestoreCharacter)
                                        }
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete"
                                )
                            }
                        }

                    }
                }
            }
        }
    }
}


@Composable
fun DeleteAndUndoButton() {

}