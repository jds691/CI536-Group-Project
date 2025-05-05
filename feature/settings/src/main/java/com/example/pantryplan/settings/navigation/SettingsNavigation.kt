package com.example.pantryplan.settings.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.pantryplan.settings.SettingsScreen
import kotlinx.serialization.Serializable

@Serializable
data object SettingsBase

@Serializable
data object Settings

fun NavController.navigateToSettings() = navigate(route = Settings)

fun NavGraphBuilder.settingsSection(
    onBackClick: () -> Unit
) {
    navigation<SettingsBase>(startDestination = Settings) {
        composable<Settings> {
            SettingsScreen(
                onBackClick = onBackClick
            )
        }
    }
}