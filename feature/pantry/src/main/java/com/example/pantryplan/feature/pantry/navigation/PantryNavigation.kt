package com.example.pantryplan.feature.pantry.navigation

import kotlinx.serialization.Serializable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.pantryplan.feature.pantry.PantryScreen

@Serializable object PantryRoute // Route to Pantry screen

fun NavGraphBuilder.pantryScreen() {
    composable<PantryRoute> {
        PantryScreen()
    }
}