package com.example.pantryplan.settings.navigation

import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.pantryplan.settings.AllergiesSettingsScreen
import com.example.pantryplan.settings.DebugSettingsScreen
import com.example.pantryplan.settings.MealPlannerSettingsScreen
import com.example.pantryplan.settings.PantrySettingsScreen
import com.example.pantryplan.settings.SettingsScreen
import com.example.pantryplan.settings.SettingsViewModel
import kotlinx.serialization.Serializable

@Serializable
data object SettingsBase
@Serializable
data object Settings
@Serializable
data object AllergySettings
@Serializable
data object PantrySettings
@Serializable
data object MealPlannerSettings

@Serializable
data object DebugSettings

fun NavController.navigateToSettings() = navigate(route = Settings)
fun NavController.navigateToAllergySettings() = navigate(route = AllergySettings)
fun NavController.navigateToPantrySettings() = navigate(route = PantrySettings)
fun NavController.navigateToMealPlannerSettings() = navigate(route = MealPlannerSettings)
fun NavController.navigateToDebugSettings() = navigate(route = DebugSettings)

fun NavGraphBuilder.settingsSection(
    navController: NavController,
    onBackClick: () -> Unit,
    onAllergySettingsClick: () -> Unit,
    onPantrySettingsClick: () -> Unit,
    onMealPlannerSettingsClick: () -> Unit,
    onDebugSettingsClick: () -> Unit
) {
    navigation<SettingsBase>(startDestination = Settings) {
        composable<Settings> {
            val parentEntry = remember(it) {
                navController.getBackStackEntry(SettingsBase)
            }

            val settingsViewModel: SettingsViewModel = hiltViewModel(parentEntry)

            SettingsScreen(
                viewModel = settingsViewModel,
                onBackClick = onBackClick,
                onAllergySettingsClick = onAllergySettingsClick,
                onPantrySettingsClick = onPantrySettingsClick,
                onMealPlannerSettingsClick = onMealPlannerSettingsClick,
                onDebugSettingsClick = onDebugSettingsClick
            )
        }

        composable<AllergySettings> {
            val parentEntry = remember(it) {
                navController.getBackStackEntry(SettingsBase)
            }

            val settingsViewModel: SettingsViewModel = hiltViewModel(parentEntry)

            AllergiesSettingsScreen(
                viewModel = settingsViewModel,
                onBackClick = onBackClick
            )
        }

        composable<PantrySettings> {
            val parentEntry = remember(it) {
                navController.getBackStackEntry(SettingsBase)
            }

            val settingsViewModel: SettingsViewModel = hiltViewModel(parentEntry)

            PantrySettingsScreen(
                viewModel = settingsViewModel,
                onBackClick = onBackClick
            )
        }

        composable<MealPlannerSettings> {
            val parentEntry = remember(it) {
                navController.getBackStackEntry(SettingsBase)
            }

            val settingsViewModel: SettingsViewModel = hiltViewModel(parentEntry)

            MealPlannerSettingsScreen(
                viewModel = settingsViewModel,
                onBackClick = onBackClick
            )
        }

        composable<DebugSettings> {
            val parentEntry = remember(it) {
                navController.getBackStackEntry(SettingsBase)
            }

            val settingsViewModel: SettingsViewModel = hiltViewModel(parentEntry)

            DebugSettingsScreen(
                viewModel = settingsViewModel,
                onBackClick = onBackClick
            )
        }
    }
}