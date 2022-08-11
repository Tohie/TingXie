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
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import com.example.tingxie.R

import com.example.tingxie.presentation.character_quiz.CharacterQuizEvents
import com.example.tingxie.presentation.character_quiz.CharactersQuizViewModel
import com.example.tingxie.presentation.edit_character.EditCharacterViewModel
import com.example.tingxie.presentation.util.CharacterDetail
import com.example.tingxie.presentation.util.Screen
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest

@Composable
@OptIn(ExperimentalPagerApi::class)
fun CharacterQuizScreen(
    navController: NavController,
    viewModel: CharactersQuizViewModel = hiltViewModel()
) {
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(initialPage = viewModel.state.value.currentCharacter)

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

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }
            .collect { pageIndex -> viewModel.onEvent(CharacterQuizEvents.PageChange(pageIndex))}
    }

    Scaffold(
        scaffoldState = scaffoldState,
        bottomBar = {
            BottomAppBar {


            }
        }
){
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
             HorizontalPager(
                 count = viewModel.state.value.characters.size,
                 modifier = Modifier
                     .fillMaxSize(),
                 state = pagerState,
             ) { pageIndex ->
                 val currentCharacter = viewModel.state.value.characters.get(pageIndex)

                 Column {
                    CharacterDetail(
                        character = currentCharacter.character,
                        modifier = Modifier,
                        showCharacter = currentCharacter.isVisibile
                    )
                     Row(
                         modifier = Modifier
                             .fillMaxSize(),
                         horizontalArrangement = Arrangement.Center
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
                                     pageIndex
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
             }

        }
    }
}