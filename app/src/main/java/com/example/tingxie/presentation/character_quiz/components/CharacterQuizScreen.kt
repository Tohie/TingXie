package com.example.tingxie.presentation.character_quiz.components

import android.util.Log
import android.widget.NumberPicker
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.compose.ui.util.lerp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import com.example.tingxie.domain.model.util.ChooseCharactersBy

import com.example.tingxie.presentation.character_quiz.CharacterQuizEvents
import com.example.tingxie.presentation.character_quiz.CharactersQuizViewModel
import com.example.tingxie.presentation.character_quiz.StartQuizItem
import com.example.tingxie.presentation.util.*
import com.google.accompanist.pager.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

@Composable
fun CharacterQuizScreen(
    navController: NavController,
    viewModel: CharactersQuizViewModel = hiltViewModel()
) {
    val scaffoldState = rememberScaffoldState()

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is CharactersQuizViewModel.UiEvent.SaveAndFinishQuiz -> {
                    navController.navigate(Screen.QuizResultsScreen.route)
                }
                is CharactersQuizViewModel.UiEvent.QuitQuiz -> {
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
        if (viewModel.state.value.isQuitWithoutSavingDialogueVisible) {
            QuitWithoutSavingDialogue(viewModel = viewModel)
        }
        if (viewModel.state.value.characters.isEmpty()) {
            StartQuizPage(viewModel)
        } else {
            Pager(viewModel = viewModel)
        }
    }
}

@Composable
private fun StartQuizPage(viewModel: CharactersQuizViewModel) {
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
        TestExplanation()

        CharacterAmountControls(viewModel)

        StartQuiz(viewModel, modifier = Modifier.weight(0.1f))

        CategoryControls(viewModel)
    }
}

@Composable
private fun CategoryControls(viewModel: CharactersQuizViewModel) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "From: ",
            fontSize = 10.sp
        )
        CategoryDropDown(
            categories = viewModel.state.value.categories,
            onClick = { viewModel.onEvent(CharacterQuizEvents.ChangeCategory(it)) },
            includeNoneOption = true,
            onNoneClicked = { viewModel.onEvent(CharacterQuizEvents.ChangeCategory(null)) }
        ) {
            Text(text = viewModel.state.value.currentCategory?.categoryName ?: run { "None" })
        }
    }
}

@Composable
private fun StartQuiz(viewModel: CharactersQuizViewModel, modifier: Modifier = Modifier) {
    StartQuizButtons(
        viewModel = viewModel,
        startQuizItems = listOf(
            StartQuizItem(
                text = "Random",
                chooseCharactersBy = ChooseCharactersBy.Random(viewModel.state.value.numberOfCharacters),
            ),
            StartQuizItem(
                text = "Least\nTested",
                chooseCharactersBy = ChooseCharactersBy.LeastTested(viewModel.state.value.numberOfCharacters),
            ),
            StartQuizItem(
                text = "Most\nIncorrect",
                chooseCharactersBy = ChooseCharactersBy.MostIncorrect(viewModel.state.value.numberOfCharacters),
            ),
            StartQuizItem(
                text = "Least\nCorrect",
                chooseCharactersBy = ChooseCharactersBy.LeastCorrect(viewModel.state.value.numberOfCharacters),
            )
        )
    ) {
        Text(
            text = "Test by:",
            modifier = modifier,
            fontSize = 10.sp
        )
    }
}

@Composable
private fun TestExplanation() {
    Text(text = "Let's start a quiz!", fontSize = 20.sp)
    Spacer(modifier = Modifier.height(8.dp))
    Text(
        text = "Use a pen and paper to write the answers for each question, use the eye to see the correct answer and choose right or wrong",
        textAlign = TextAlign.Justify,
    )
}

@Composable
private fun CharacterAmountControls(viewModel: CharactersQuizViewModel) {
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
}

@Composable
fun StartQuizButtons(
    viewModel: CharactersQuizViewModel,
    startQuizItems: List<StartQuizItem>,
    additionalInfo: @Composable () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        additionalInfo()

        startQuizItems.forEach{ startQuizItem ->
            StartQuizButton(
                text = startQuizItem.text,
                chooseCharactersBy = startQuizItem.chooseCharactersBy,
                viewModel = viewModel,
                modifier = Modifier.weight(0.85f/startQuizItems.size, fill = true)
            )
        }
    }
}

@Composable
fun StartQuizButton(
    text: String,
    chooseCharactersBy: ChooseCharactersBy,
    viewModel: CharactersQuizViewModel,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = { viewModel.onEvent(CharacterQuizEvents.StartQuiz(chooseCharactersBy)) },
        modifier = modifier
    ) {
        Text(text = text, fontSize = 10.sp)
    }
}

@Composable
fun QuitWithoutSavingDialogue(
    viewModel: CharactersQuizViewModel,
) {
    val onDismiss: () -> Unit =  {
        viewModel.onEvent(CharacterQuizEvents.ChangeQuitWithoutSavingDialogueVisibility(false))
    }
    Dialog(onDismissRequest = { onDismiss() }) {
        Card(
            elevation = 12.dp,
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "Quit without saving")
                Text(text = "Are you sure all progress will be lost!")
                Row(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Button(onClick = { viewModel.onEvent(CharacterQuizEvents.QuitWithoutSaving) }) {
                        Text(
                            text = "Confirm quit without saving",
                            fontSize = 10.sp,
                        )
                    }
                    Button(onClick = { onDismiss() }) {
                        Text(
                            text = "Continue quiz",
                            fontSize = 10.sp,
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun Pager(viewModel: CharactersQuizViewModel) {
    val pagerState = rememberPagerState()
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
        if (viewModel.state.value.characters.isEmpty()) return@VerticalPager
        val currentCharacter = viewModel.state.value.characters[pageIndex]

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
                showCharacter = currentCharacter.isVisible,
                AdditionalContent = {
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
                },
                Categories = {}
            )
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
            IconButton(onClick = { viewModel.onEvent(CharacterQuizEvents.SaveQuizResults) }) {
                Icon(
                    imageVector = Icons.Default.Save,
                    contentDescription = "Save and exit quiz"
                )
            }
            IconButton(onClick = { viewModel.onEvent(CharacterQuizEvents.ChangeQuitWithoutSavingDialogueVisibility(isVisible = true)) }) {
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