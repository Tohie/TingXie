package com.example.tingxie.presentation.edit_character

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.flow.collectLatest

@Composable
fun EditCharacterScreen(
    navController: NavController,
    viewModel: EditCharacterViewModel = hiltViewModel()
) {
    val characterState = viewModel.character.value
    val pinyinState = viewModel.pinyin.value
    val descriptionState = viewModel.description.value

    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                EditCharacterViewModel.UiEvent.SaveCharacter -> {
                    navController.navigateUp()
                }
                is EditCharacterViewModel.UiEvent.ShowSnackbar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message
                    )
                }
            }
        }
    }
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                viewModel.onEvent(EditCharacterEvent.SaveCharacter)
            },
            backgroundColor = MaterialTheme.colors.primary
            ) {
                Icon(imageVector = Icons.Default.Done, contentDescription = "Save Note")
            }
        },
        scaffoldState = scaffoldState
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            TransparentHintTextField(
                text = characterState.text,
                hint = characterState.hint,
                onValueChange = { viewModel.onEvent(EditCharacterEvent.EnteredCharacter(it)) },
                onFocusChange = { viewModel.onEvent(EditCharacterEvent.ChangeCharacterFocus(it)) },
                isHintVisible = characterState.isHintVisible,
                singleLine = true,
                textStyle = MaterialTheme.typography.h5
            )
            Spacer(modifier = Modifier.height(16.dp))

            TransparentHintTextField(
                text = pinyinState.text,
                hint = pinyinState.hint,
                onValueChange = { viewModel.onEvent(EditCharacterEvent.EnteredPinyin(it)) },
                onFocusChange = { viewModel.onEvent(EditCharacterEvent.ChangePinyinFocus(it)) },
                isHintVisible = pinyinState.isHintVisible,
                singleLine = true,
                textStyle = MaterialTheme.typography.h5
            )
            Spacer(modifier = Modifier.height(16.dp))

            TransparentHintTextField(
                text = descriptionState.text,
                hint = descriptionState.hint,
                onValueChange = { viewModel.onEvent(EditCharacterEvent.EnteredDescription(it)) },
                onFocusChange = { viewModel.onEvent(EditCharacterEvent.ChangeDescriptionFocus(it)) },
                isHintVisible = descriptionState.isHintVisible,
                singleLine = true,
                textStyle = MaterialTheme.typography.h5
            )
        }
    }
}