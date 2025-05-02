package com.example.pantryplan.feature.meals.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.pantryplan.feature.meals.MealPlannerScreen
import com.example.pantryplan.feature.meals.NutritionalDetailsScreen
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
object MealPlannerBase // Parent route for Meal Planner screen

@Serializable
data object MealPlanner // Route to Meal Planner screen

@Serializable
data object NutritionalDetails // Route to nutritional details screen

fun NavController.navigateToMealPlanner() = navigate(route = MealPlanner)
fun NavController.navigateToNutritionalDetails() = navigate(route = NutritionalDetails)

fun NavGraphBuilder.mealPlannerScreen(
    onBackClick: () -> Unit,
    onRecipeClick: (UUID) -> Unit,
    onMacroCardClick: () -> Unit
) {
    navigation<MealPlannerBase>(startDestination = MealPlanner) {
        composable<MealPlanner> {
            MealPlannerScreen(
                onRecipeClick = onRecipeClick,
                onMacroCardClick = onMacroCardClick
            )
        }

        composable<NutritionalDetails> {
            NutritionalDetailsScreen(
                onBackClick = onBackClick
            )
        }
    }
}