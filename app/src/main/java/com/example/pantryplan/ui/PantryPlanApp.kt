@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.pantryplan.ui

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import com.example.pantryplan.feature.meals.navigation.mealPlannerScreen
import com.example.pantryplan.feature.meals.navigation.navigateToNutritionalDetails
import com.example.pantryplan.feature.pantry.navigation.Pantry
import com.example.pantryplan.feature.pantry.navigation.navigateToPantryItem
import com.example.pantryplan.feature.pantry.navigation.navigateToPantryItemEdit
import com.example.pantryplan.feature.pantry.navigation.pantrySection
import com.example.pantryplan.feature.recipes.navigation.navigateToRecipeItem
import com.example.pantryplan.feature.recipes.navigation.navigateToRecipeItemAdd
import com.example.pantryplan.feature.recipes.navigation.recipeSection
import com.example.pantryplan.navigation.TopLevelDestination
import com.example.pantryplan.settings.navigation.navigateToAllergySettings
import com.example.pantryplan.settings.navigation.navigateToMealPlannerSettings
import com.example.pantryplan.settings.navigation.navigateToPantrySettings
import com.example.pantryplan.settings.navigation.navigateToSettings
import com.example.pantryplan.settings.navigation.settingsSection

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun PantryPlanApp(appState: PantryPlanAppState) {
    val currentDestination = appState.currentDestination
    val destination = appState.currentTopLevelDestination

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
                        IconButton(onClick = appState.navController::navigateToSettings) {
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
        bottomBar = {
            AnimatedVisibility(
                visible = destination != null,
                enter = EnterTransition.None,
                // Same as the default animation spec used by the NavHost.
                exit = fadeOut(tween(700))
            ) {
                NavigationBar {
                    TopLevelDestination.entries.forEach { topLevelDestination ->
                        val selected = currentDestination?.hierarchy?.any {
                            it.hasRoute(topLevelDestination.route)
                        } == true

                        NavigationBarItem(
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
        }
    ) { _ ->
        NavHost(
            navController = appState.navController,
            startDestination = Pantry,
        ) {
            pantrySection(
                onPantryItemClick = appState.navController::navigateToPantryItem,
                onBackClick = appState.navController::popBackStack,
                onPantryItemEdit = appState.navController::navigateToPantryItemEdit
            )
            recipeSection(
                onRecipeItemClick = appState.navController::navigateToRecipeItem,
                onRecipeItemAdd = appState.navController::navigateToRecipeItemAdd,
                onBackClick = appState.navController::popBackStack
            )
            mealPlannerScreen(
                onBackClick = appState.navController::popBackStack,
                onRecipeClick = {}, // TODO: Implement when we have recipe sub-navigation done
                onMacroCardClick = appState.navController::navigateToNutritionalDetails
            )

            settingsSection(
                // Required to be able to share 1 common view model between all settings screens
                navController = appState.navController,

                onBackClick = appState.navController::popBackStack,
                onAllergySettingsClick = appState.navController::navigateToAllergySettings,
                onPantrySettingsClick = appState.navController::navigateToPantrySettings,
                onMealPlannerSettingsClick = appState.navController::navigateToMealPlannerSettings
            )
        }
    }
}