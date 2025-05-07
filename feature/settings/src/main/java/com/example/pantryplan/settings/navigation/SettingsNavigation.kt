package com.example.pantryplan.settings.navigation

import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.pantryplan.settings.AllergiesSettingsScreen
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

fun NavController.navigateToSettings() = navigate(route = Settings)
fun NavController.navigateToAllergySettings() = navigate(route = AllergySettings)
fun NavController.navigateToPantrySettings() = navigate(route = PantrySettings)

fun NavGraphBuilder.settingsSection(
    navController: NavController,
    onBackClick: () -> Unit,
    onAllergySettingsClick: () -> Unit,
    onPantrySettingsClick: () -> Unit
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
                onPantrySettingsClick = onPantrySettingsClick
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
    }
}