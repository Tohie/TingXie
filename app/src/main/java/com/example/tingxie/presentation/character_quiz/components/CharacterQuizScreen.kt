package com.example.tingxie.presentation.character_quiz.components

import android.util.Log
import android.widget.NumberPicker
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.compose.ui.util.lerp
import androidx.compose.ui.viewinterop.AndroidView

import com.example.tingxie.presentation.character_quiz.CharacterQuizEvents
import com.example.tingxie.presentation.character_quiz.CharactersQuizViewModel
import com.example.tingxie.presentation.util.BottomBar
import com.example.tingxie.presentation.util.CharacterDetail
import com.example.tingxie.presentation.util.Screen
import com.example.tingxie.presentation.util.TopBar
import com.google.accompanist.pager.*
import io.ak1.drawbox.rememberDrawController
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

@OptIn(ExperimentalMaterialApi::class)
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
        topBar = { TopBar { SaveAndHomeFunctions(viewModel = viewModel) } },
        bottomBar = { BottomBar(navController = navController ) },
        modifier = Modifier.fillMaxSize(),

    ) {
        if (viewModel.state.value.characters.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Text(text = "Let's start a quiz!")
                Text(
                    text = "Use a pen and paper to write the answers for each question, use the eye to see the correct answer and choose right or wrong",
                    textAlign = TextAlign.Justify,
                )
                AndroidView(
                    factory = { context ->
                        NumberPicker(context).apply {
                            minValue = 1
                            maxValue = 20
                            wrapSelectorWheel = true

                            setOnValueChangedListener { _, _, newVal ->
                                Log.i("character", "changed scroll wheel")
                                viewModel.onEvent(CharacterQuizEvents.ChangeCharacterNumber(newVal))
                            }
                            value = 5
                        }
                    },
                    update = { }
                )
                Button(onClick = { viewModel.onEvent(CharacterQuizEvents.StartQuiz) }) {
                    Text(text = "Let's Start")
                }
            }
        } else {
            Pager(viewModel = viewModel)
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun Pager(viewModel: CharactersQuizViewModel) {
    val pagerState = rememberPagerState()
    val drawController = rememberDrawController()
    val scope = rememberCoroutineScope()

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
                showCharacter = currentCharacter.isVisible
            ) {
                QuizCardOptions(
                    viewModel = viewModel,
                    currentCharacter = currentCharacter,
                    pageIndex = pageIndex,
                    changePage = {
                        scope.launch {
                            pagerState.animateScrollToPage(pageIndex+1)
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun SaveAndHomeFunctions(viewModel: CharactersQuizViewModel, modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        Row (
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
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
fun QuizCardOptions(viewModel: CharactersQuizViewModel, currentCharacter: CharacterState, pageIndex: Int, changePage: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        ChangeCharacterCorrectnessButton(
            viewModel = viewModel,
            pageIndex = pageIndex,
            changePage = changePage,
            isCorrect = true
        )

        ChangeCharacterCorrectnessButton(
            viewModel = viewModel,
            pageIndex = pageIndex,
            changePage = changePage,
            isCorrect = false
        )

        IconButton(onClick = {
            viewModel.onEvent(
                CharacterQuizEvents.ChangeCharacterVisibility(
                    pageIndex,
                    !currentCharacter.isVisible
                )
            )
        }) {
            Icon(
                imageVector = if (!currentCharacter.isVisible) {
                    Icons.Default.Visibility
                } else {
                    Icons.Default.VisibilityOff
                },
                contentDescription = "Show/hide"
            )
        }

    }
}

@Composable
fun ChangeCharacterCorrectnessButton(
    viewModel: CharactersQuizViewModel,
    pageIndex: Int,
    changePage: () -> Unit,
    isCorrect: Boolean
) {
    IconButton(onClick = {
        viewModel.onEvent(
            CharacterQuizEvents.ChangeCharacterCorrect(
                pageIndex,
                isCorrect
            )
        )
        changePage()
    }) {
        Icon(
            imageVector = if (isCorrect)  Icons.Default.Done else Icons.Default.Close,
            contentDescription = if (isCorrect) "Correct" else "Incorrect"
        )
    }
}