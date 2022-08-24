package com.example.tingxie.presentation.util

import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.QueryStats
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomNavigationBar(
    items: List<BottomNavItem>,
    navController: NavController,
    modifier: Modifier = Modifier,
    onItemClick: (BottomNavItem) -> Unit
) {
    val backStackEntry = navController.currentBackStackEntryAsState()
    BottomNavigation(
        modifier = modifier,
        elevation = 5.dp
    ) {
        items.forEach { item ->
            val selected = backStackEntry.value?.destination?.route?.startsWith(item.route, true)?: false
            BottomNavigationItem(
                selected = selected,
                onClick = { onItemClick(item) },
                selectedContentColor = Color.White,
                unselectedContentColor = Color.Gray,
                icon = {
                    Column(horizontalAlignment = CenterHorizontally) {
                        Icon(imageVector = item.icon, contentDescription = item.name)
                        if (selected) {
                            Text(
                                text = item.name,
                                textAlign = TextAlign.Center,
                                fontSize = 10.sp
                            )
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun BottomBar(navController: NavController) {
    BottomNavigationBar(
        items = listOf(
            BottomNavItem(
                name = "Home",
                route = Screen.CharactersScreen.route,
                icon = Icons.Default.Home
            ),
            BottomNavItem(
                name = "Add",
                route = Screen.EditCharacterScreen.route,
                icon = Icons.Default.Add
            ),
            BottomNavItem(
                name = "Test",
                route = Screen.CharacterQuizScreen.route,
                icon = Icons.Default.Checklist
            ),
            BottomNavItem(
                name = "Stats",
                route = Screen.QuizStatisticsScreen.route,
                icon = Icons.Default.QueryStats
            ),
        ),
        navController = navController,
        onItemClick = { item ->
            navController.navigate(item.route)
        }
    )
}