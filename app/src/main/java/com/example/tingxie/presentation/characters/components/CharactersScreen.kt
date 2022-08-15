package com.example.tingxie.presentation.characters.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.tingxie.domain.model.Character
import com.example.tingxie.presentation.characters.CharactersEvent
import com.example.tingxie.presentation.characters.CharactersState
import com.example.tingxie.presentation.characters.CharactersViewModel
import com.example.tingxie.presentation.edit_character.TransparentHintTextField
import com.example.tingxie.presentation.util.CharacterDetail
import com.example.tingxie.presentation.util.Screen
import com.example.tingxie.presentation.util.TopBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
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
        bottomBar = { BottomBar(navController) },
        scaffoldState = scaffoldState
    ) {
        Box(
            modifier = Modifier.fillMaxSize().padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    BasicTextField(
                        value = "",
                        onValueChange = { /* TODO */},
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp)
                            .border(BorderStroke(4.dp, Color.Black))
                    )
                    IconButton(
                        onClick = { /*TODO*/ },
                    ) {
                        Icon(imageVector = Icons.Default.Expand, contentDescription = "Expand search options")
                    }
                }
                CharacterScreenCharacterList(
                    state = state,
                    viewModel = viewModel,
                    scope = scope,
                    scaffoldState = scaffoldState,
                    navController = navController
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun CharacterScreenCharacterList(
    state: CharactersState,
    viewModel: CharactersViewModel,
    scope: CoroutineScope,
    scaffoldState: ScaffoldState,
    navController: NavController,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        items(
            items = state.characters,
            key = { item: Character -> item.id!! }
        ) { character ->
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CharacterScreenCharacterDetail(
                    character = character,
                    viewModel = viewModel,
                    scope = scope,
                    scaffoldState = scaffoldState,
                    modifier = Modifier
                        .animateItemPlacement()
                        .clickable {
                            navController.navigate(
                                Screen.EditCharacterScreen.route + "?characterId=${character.id}"
                            )
                        }
                        .padding(4.dp),
                )
            }
        }
    }
}

@Composable
private fun CharacterScreenCharacterDetail(
    character: Character,
    viewModel: CharactersViewModel,
    scope: CoroutineScope,
    scaffoldState: ScaffoldState,
    modifier: Modifier
) {
    CharacterDetail(
        character = character,
        modifier = modifier,
        showCharacter = true
    ) {
        EndAlignedDeleteButton(viewModel, character, scope, scaffoldState)
    }
}

@Composable
private fun EndAlignedDeleteButton(
    viewModel: CharactersViewModel,
    character: Character,
    scope: CoroutineScope,
    scaffoldState: ScaffoldState
) {
    Row(
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


@Composable
fun BottomBar(navController: NavController) {
    BottomAppBar() {
        Row(
            modifier = Modifier
                .fillMaxSize(),
            horizontalArrangement = Arrangement.Center
        ) {
            NavigationIconButton(
                navController = navController,
                route = Screen.EditCharacterScreen.route,
                icon = Icons.Default.Add,
                contentDescription = "Add new note"
            )

            NavigationIconButton(
                navController = navController,
                route = Screen.CharacterQuizScreen.route + "?characterNumber=20",
                icon = Icons.Default.Checklist,
                contentDescription = "Start a test"
            )

            GoToTestPage(navController = navController, numberOfCharacter = 5)
            GoToTestPage(navController = navController, numberOfCharacter = 10)
            GoToTestPage(navController = navController, numberOfCharacter = 15)

            NavigationIconButton(
                navController = navController,
                route = Screen.QuizStatisticsScreen.route,
                icon = Icons.Default.QueryStats,
                contentDescription = "See quiz results"
            )
        }
    }
}

@Composable
fun NavigationIconButton(
    navController: NavController,
    route: String,
    icon: ImageVector,
    contentDescription: String
) {
    IconButton(
        onClick = {
            navController.navigate(route)
        }
    ) {
        Icon(imageVector = icon, contentDescription = contentDescription)
    }
}

@Composable
fun GoToTestPage(navController: NavController, numberOfCharacter: Int) {
    Button(
        onClick = {
            navController.navigate(Screen.CharacterQuizScreen.route + "?characterNumber=${numberOfCharacter}")
        }
    ) {
        Text(text = numberOfCharacter.toString())
    }
}