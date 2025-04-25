package com.example.pantryplan.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
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
@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalComposeUiApi::class,
)
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
                        contentDescription = stringResource(topLevelDestination.label)
                    )},
                    label = { Text(stringResource(topLevelDestination.label)) },
                )
            }
        }
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                val destination = appState.currentTopLevelDestination
                if (destination != null) {
                    CenterAlignedTopAppBar(
                        title = {
                            Text(stringResource(destination.label))
                        },
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