package com.example.tingxie.presentation.util

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp

@Composable
fun OutlinedHintTextField(
    text: String,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
    onValueChange: (String) -> Unit,
    textStyle: TextStyle = TextStyle(),
    singleLine: Boolean = false,
    hint: String
) {
    Box(modifier = modifier) {
        OutlinedTextField(
            value = text,
            onValueChange = onValueChange,
            singleLine = singleLine,
            textStyle = textStyle,
            modifier = Modifier
                .fillMaxWidth(),
            keyboardOptions = keyboardOptions,
            placeholder = {
                Text(
                    text = hint,
                    style = textStyle,
                    fontSize = 20.sp
                )
            }
        )
        /*
        Spacer(modifier = Modifier.width(4.dp))
        if (isHintVisible) {
            Text(
                text = hint,
                style = textStyle,
                modifier = Modifier.offset(x = 10.dp, y = 13.dp),
                fontSize = 20.sp
            )
        }
         */
    }
}