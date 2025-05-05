package com.example.pantryplan.settings.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.pantryplan.settings.AllergiesSettingsScreen
import com.example.pantryplan.settings.SettingsScreen
import kotlinx.serialization.Serializable

@Serializable
data object SettingsBase

@Serializable
data object Settings

@Serializable
data object AllergySettings

fun NavController.navigateToSettings() = navigate(route = Settings)
fun NavController.navigateToAllergySettings() = navigate(route = AllergySettings)

fun NavGraphBuilder.settingsSection(
    onBackClick: () -> Unit,
    onAllergySettingsClick: () -> Unit
) {
    navigation<SettingsBase>(startDestination = Settings) {
        composable<Settings> {
            SettingsScreen(
                onBackClick = onBackClick,
                onAllergySettingsClick = onAllergySettingsClick
            )
        }

        composable<AllergySettings> {
            AllergiesSettingsScreen(
                onBackClick = onBackClick
            )
        }
    }
}