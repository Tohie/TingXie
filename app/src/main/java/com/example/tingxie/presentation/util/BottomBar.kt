package com.example.tingxie.presentation.util

import android.transition.Scene
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.QueryStats
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Checklist
import androidx.compose.material.icons.outlined.Home
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.Navigation
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottomBar(navController: NavController) {
    val scope = rememberCoroutineScope()

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

@Composable
fun NavigationIconButton(
    navController: NavController,
    route: String,
    isCurrentRoute: Boolean = false,
    currentRouteIcon: ImageVector,
    currentLabel: String = "",
    nonCurrentRouteIcon: ImageVector,
    contentDescription: String,
) {
    Column {
        IconButton(
            onClick = {
                Log.i("character", "current route: ${isCurrentRoute}")
                navController.navigate(route) {
                    // Pop up to the start destination of the graph to
                    // avoid building up a large stack of destinations
                    // on the back stack as users select items
                    navController.graph.startDestinationRoute?.let { route ->
                        popUpTo(route) {
                            saveState = true
                        }
                    }
                    // Avoid multiple copies of the same destination when
                    // reselecting the same item
                    launchSingleTop = true
                    // Restore state when reselecting a previously selected item
                    restoreState = true
                }
            },
        ) {
            Icon(
                imageVector = if (isCurrentRoute) currentRouteIcon else nonCurrentRouteIcon,
                contentDescription = contentDescription,

            )
        }
        if (isCurrentRoute) {
            Text(text = currentLabel)
        }
    }
}