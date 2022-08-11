package com.example.tingxie.presentation.util

import android.graphics.drawable.Icon
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.tingxie.domain.model.Character

@Composable
fun CharacterDetail(
    character: Character,
    modifier: Modifier = Modifier,
    showCharacter: Boolean = true
) {
    Row {
        if (showCharacter) {
            Text(
                text = character.character,
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.onSurface,
            )
        }
        Column() {
            Text(
                text = character.pinyin,
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.onSurface,
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = character.description,
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.onSurface,
            )
        }
    }
}