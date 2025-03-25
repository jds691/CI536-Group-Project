package com.example.pantryplan.feature.recipes.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.pantryplan.feature.recipes.RecipesScreen
import kotlinx.serialization.Serializable

@Serializable object RecipesRoute

fun NavController.navigateToRecipes() = navigate(route = RecipesRoute)

fun NavGraphBuilder.recipesScreen() {
    composable<RecipesRoute> {
        RecipesScreen()
    }
}