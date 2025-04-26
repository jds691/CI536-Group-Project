@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.pantryplan.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import com.example.pantryplan.feature.pantry.navigation.PantryRoute
import com.example.pantryplan.feature.pantry.navigation.pantryScreen
import com.example.pantryplan.feature.recipes.navigation.recipesScreen
import com.example.pantryplan.navigation.TopLevelDestination
import com.example.pantryplan.ui.theme.PantryPlanTheme
import com.example.pantryplan.feature.meals.navigation.mealPlannerScreen

@Composable
fun PantryPlanApp(appState: PantryPlanAppState) {
    val currentDestination = appState.currentDestination

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            TopLevelDestination.entries.forEach { topLevelDestination ->
                val selected = currentDestination?.hierarchy?.any {
                    it.hasRoute(topLevelDestination.route)
                } == true

                item(
                    selected = selected,
                    onClick = { appState.navigateToTopLevelDestination(topLevelDestination) },
                    icon = { Icon(
                        if (selected) topLevelDestination.selectedIcon else topLevelDestination.unselectedIcon,
                        contentDescription = stringResource(topLevelDestination.iconTextId)
                    )},
                    label = { Text(stringResource(topLevelDestination.iconTextId)) },
                )
            }
        }
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                val destination = appState.currentTopLevelDestination
                if (destination != null) {
                    val shouldShowSearchButton = destination != TopLevelDestination.MEAL_PLANNER

                    CenterAlignedTopAppBar(
                        navigationIcon = {
                            if (shouldShowSearchButton) {
                                IconButton(onClick = { /* TODO: Navigate to search. */ }) {
                                    Icon(
                                        imageVector = Icons.Default.Search,
                                        // TODO: Replace with a string resource from settings feature.
                                        contentDescription = "Search"
                                    )
                                }
                            }
                        },
                        title = { Text(stringResource(destination.titleTextId)) },
                        actions = {
                            IconButton(onClick = { /* TODO: Navigate to settings. */ }) {
                                Icon(
                                    imageVector = Icons.Default.AccountCircle,
                                    // TODO: Replace with a string resource from settings feature.
                                    contentDescription = "Settings"
                                )
                            }
                        }
                    )
                }
            },
        ) { innerPadding ->
            NavHost(
                navController = appState.navController,
                startDestination = PantryRoute,
                modifier = Modifier.padding(innerPadding)
            ) {
                pantryScreen()
                recipesScreen()
                mealPlannerScreen()
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PantryPlanTheme {
        Greeting("Android")
    }
}