package com.example.tingxie.presentation.util

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottomBar(navController: NavController) {
    val scope = rememberCoroutineScope()
    val navBarBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBarBackStackEntry?.destination?.route

    BottomAppBar() {
        Row(
            modifier = Modifier
                .fillMaxSize(),
            horizontalArrangement = Arrangement.Center
        ) {
            NavigationIconButton(
                navController = navController,
                route = Screen.CharactersScreen.route,
                isCurrentRoute = Screen.CharactersScreen.route == currentRoute,
                currentRouteIcon = Icons.Default.Home,
                nonCurrentRouteIcon = Icons.Outlined.Home,
                currentLabel = "Home",
                contentDescription = "Add new note"
            )

            NavigationIconButton(
                navController = navController,
                route = Screen.EditCharacterScreen.route,
                isCurrentRoute = Screen.EditCharacterScreen.route == currentRoute,
                currentRouteIcon = Icons.Default.Add,
                nonCurrentRouteIcon = Icons.Outlined.Add,
                currentLabel = "Add",
                contentDescription = "Add new note"
            )

            NavigationIconButton(
                navController = navController,
                route = Screen.CharacterQuizScreen.route,
                isCurrentRoute = Screen.CharacterQuizScreen.route == currentRoute,
                currentRouteIcon = Icons.Default.Checklist,
                nonCurrentRouteIcon = Icons.Outlined.Checklist,
                currentLabel = "Test",
                contentDescription = "Add new note"
            )

            NavigationIconButton(
                navController = navController,
                route = Screen.QuizStatisticsScreen.route,
                isCurrentRoute = Screen.QuizStatisticsScreen.route == currentRoute,
                currentRouteIcon = Icons.Default.QueryStats,
                nonCurrentRouteIcon = Icons.Filled.QueryStats,
                currentLabel = "Stats",
                contentDescription = "See quiz results"
            )
        }
    }
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