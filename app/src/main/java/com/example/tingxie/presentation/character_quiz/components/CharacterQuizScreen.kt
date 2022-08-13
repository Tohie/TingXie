package com.example.tingxie.presentation.character_quiz.components

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.compose.ui.util.lerp

import com.example.tingxie.presentation.character_quiz.CharacterQuizEvents
import com.example.tingxie.presentation.character_quiz.CharactersQuizViewModel
import com.example.tingxie.presentation.util.CharacterDetail
import com.example.tingxie.presentation.util.Screen
import com.example.tingxie.presentation.util.TopBar
import com.google.accompanist.pager.*
import io.ak1.drawbox.DrawBox
import io.ak1.drawbox.rememberDrawController
import kotlinx.coroutines.flow.collectLatest
import kotlin.math.absoluteValue

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
        topBar = { TopBar() },
        bottomBar = { BottomRow(viewModel) },
        modifier = Modifier.fillMaxSize(),

    ) {
        Pager(viewModel = viewModel)
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun Pager(viewModel: CharactersQuizViewModel) {
    val pagerState = rememberPagerState()
    val drawController = rememberDrawController()

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }
            .collect { pageIndex -> viewModel.onEvent(CharacterQuizEvents.PageChange(pageIndex))}
    }

    VerticalPager(
        count = viewModel.state.value.characters.size,
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        state = pagerState,
    ) { pageIndex ->
        val currentCharacter = viewModel.state.value.characters.get(pageIndex)

        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CharacterDetail(
                character = currentCharacter.character,
                modifier = Modifier
                    .padding(8.dp)
                    .graphicsLayer {
                        // Calculate the absolute offset for the current page from the
                        // scroll position. We use the absolute value which allows us to mirror
                        // any effects for both directions
                        val pageOffset = calculateCurrentOffsetForPage(pageIndex).absoluteValue

                        // We animate the scaleX + scaleY, between 85% and 100%
                        lerp(
                            start = 0.85f,
                            stop = 1f,
                            fraction = 1f - pageOffset.coerceIn(0f, 1f)
                        ).also { scale ->
                            scaleX = scale
                            scaleY = scale
                        }

                        // We animate the alpha, between 50% and 100%
                        alpha = lerp(
                            start = 0.5f,
                            stop = 1f,
                            fraction = 1f - pageOffset.coerceIn(0f, 1f)
                        )
                    },
                showCharacter = currentCharacter.isVisibile
            ) {
                QuizCardOptions(viewModel = viewModel , currentCharacter = currentCharacter , pageIndex = pageIndex)
            }

            /*
            DrawBox(
                drawController = drawController,
                modifier = Modifier
                    .padding(60.dp)
                    .fillMaxSize()
                    .weight(1f, true)
                    .border(2.dp, color = MaterialTheme.colors.primary),
                bitmapCallback = { _, _ -> Unit }
            )
            */
        }
    }
}

@Composable
fun BottomRow(viewModel: CharactersQuizViewModel) {
    BottomAppBar {
        Row (
            modifier = Modifier
                .fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Todo add a function to save and record results
            IconButton(onClick = { viewModel.onEvent(CharacterQuizEvents.SaveQuizResults) }) {
                Icon(
                    imageVector = Icons.Default.Save,
                    contentDescription = "Save and exit quiz"
                )
            }
            IconButton(onClick = { viewModel.onEvent(CharacterQuizEvents.FinishedQuiz) }) {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Exit quiz without saving"
                )
            }
        }

    }
}

@Composable
fun QuizCardOptions(viewModel: CharactersQuizViewModel, currentCharacter: CharacterState, pageIndex: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        IconButton(onClick = {
            viewModel.onEvent(
                CharacterQuizEvents.ChangeCharacterCorrect(
                    pageIndex,
                    false
                )
            )
        }) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Incorrect"
            )
        }

        IconButton(onClick = {
            viewModel.onEvent(
                CharacterQuizEvents.ChangeCharacterCorrect(
                    pageIndex,
                    true
                )
            )
        }) {
            Icon(imageVector = Icons.Default.Done, contentDescription = "Correct")
        }

        IconButton(onClick = {
            viewModel.onEvent(
                CharacterQuizEvents.ChangeCharacterVisibility(
                    pageIndex,
                    !currentCharacter.isVisibile
                )
            )
        }) {
            Icon(
                imageVector = if (!currentCharacter.isVisibile) {
                    Icons.Default.Visibility
                } else {
                    Icons.Default.VisibilityOff
                },
                contentDescription = "Show/hide"
            )
        }

    }
}