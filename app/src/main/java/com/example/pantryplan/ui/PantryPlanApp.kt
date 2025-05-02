@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.pantryplan.ui

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
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
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.material3.adaptive.navigationsuite.rememberNavigationSuiteScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import com.example.pantryplan.feature.meals.navigation.mealPlannerScreen
import com.example.pantryplan.feature.pantry.navigation.Pantry
import com.example.pantryplan.feature.pantry.navigation.navigateToPantryItem
import com.example.pantryplan.feature.pantry.navigation.navigateToPantryItemEdit
import com.example.pantryplan.feature.pantry.navigation.pantrySection
import com.example.pantryplan.feature.recipes.navigation.recipesScreen
import com.example.pantryplan.navigation.TopLevelDestination
import com.example.pantryplan.ui.theme.PantryPlanTheme

@Composable
fun PantryPlanApp(appState: PantryPlanAppState) {
    val currentDestination = appState.currentDestination
    val destination = appState.currentTopLevelDestination
    val navSuiteScaffoldState = rememberNavigationSuiteScaffoldState()

    val adaptiveInfo = currentWindowAdaptiveInfo()
    val customNavSuiteType = with(adaptiveInfo) {
        if (destination == null) {
            NavigationSuiteType.None
        } else {
            NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo(adaptiveInfo)
        }
    }

    NavigationSuiteScaffold(
        state = navSuiteScaffoldState,
        layoutType = customNavSuiteType,
        navigationSuiteItems = {
            if (destination != null) {
                TopLevelDestination.entries.forEach { topLevelDestination ->
                    val selected = currentDestination?.hierarchy?.any {
                        it.hasRoute(topLevelDestination.route)
                    } == true

                    item(
                        selected = selected,
                        onClick = { appState.navigateToTopLevelDestination(topLevelDestination) },
                        icon = {
                            Icon(
                                if (selected) topLevelDestination.selectedIcon else topLevelDestination.unselectedIcon,
                                contentDescription = stringResource(topLevelDestination.iconTextId)
                            )
                        },
                        label = { Text(stringResource(topLevelDestination.iconTextId)) },
                    )
                }
            }
        }
    ) {
        Scaffold(
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            modifier = Modifier.fillMaxSize(),
            topBar = {
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
                startDestination = Pantry,
                modifier = Modifier
                    .consumeWindowInsets(innerPadding)
                    .padding(innerPadding)
            ) {
                pantrySection(
                    onPantryItemClick = appState.navController::navigateToPantryItem,
                    onBackClick = appState.navController::popBackStack,
                    onPantryItemEdit = appState.navController::navigateToPantryItemEdit
                )
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