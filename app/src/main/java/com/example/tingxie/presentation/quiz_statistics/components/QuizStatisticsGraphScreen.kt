package com.example.tingxie.presentation.quiz_statistics.components

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.tingxie.domain.model.Quiz
import com.example.tingxie.presentation.quiz_statistics.QuizStatisticsEvent
import com.example.tingxie.presentation.quiz_statistics.QuizStatisticsState
import com.example.tingxie.presentation.quiz_statistics.QuizStatisticsViewModel
import java.util.*

@Composable
fun QuizStatisticsGraphScreen(
    viewModel: QuizStatisticsViewModel,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        Column() {
            GraphControls(
                onDateChanged = { _, year, month, dayOfMonth ->
                    viewModel.onEvent(QuizStatisticsEvent.DateChanged(year, month, dayOfMonth))
                }, onClick = { amount ->
                    viewModel.onEvent(QuizStatisticsEvent.ChangeNumberOfTestsDisplayed(amount))
                },
                state = viewModel.state
            )
            QuizStatisticsGraph(
                viewModel = viewModel,
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxSize()
            )
        }
    }
}

@Composable
fun GraphControls(
    modifier: Modifier = Modifier,
    state: State<QuizStatisticsState>,
    onDateChanged: (DatePicker, Int, Int, Int) -> Unit,
    onClick: (Int) -> Unit
) {
    Box(modifier = modifier) {
        Column {
            GraphDateSelectorControls(onDateChanged = onDateChanged, state = state)
            GraphButtons(text = "Last: ", onClick = onClick)
        }
    }
}

@Composable
fun GraphDateSelectorControls(
    modifier: Modifier = Modifier,
    onDateChanged: (DatePicker, Int, Int, Int) -> Unit,
    state: State<QuizStatisticsState>
) {
    val context = LocalContext.current
    val year = state.value.year
    val month = state.value.month
    val day = state.value.day

    val datePickerDialog = DatePickerDialog(
        context,
        onDateChanged,
        year,
        month,
        day
    )

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Text(text = "On:  ")
        Button(
            onClick = {
                datePickerDialog.show()
            }
        ) {
            Text(text = "$day/$month/$year")
        }
    }
}

@Composable
fun GraphButtons (
    text: String,
    modifier: Modifier = Modifier,
    onClick: (Int) -> Unit
) {
    Box (
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = text)
            for (i in listOf(5, 10, 15)) {
                GraphButton(onClick = { onClick(i) }, text = i.toString() )
            }
        }
    }
}

@Composable
fun GraphButton(
    onClick: () -> Unit,
    text: String
) {
    Button(onClick = onClick) {
        Text(text = text.toString())
    }
}