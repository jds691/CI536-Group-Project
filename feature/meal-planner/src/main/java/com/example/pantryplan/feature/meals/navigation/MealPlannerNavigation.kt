package com.example.pantryplan.feature.meals.navigation

import androidx.navigation.NavController
import kotlinx.serialization.Serializable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.pantryplan.feature.meals.MealPlannerScreen

@Serializable object MealPlannerRoute // Route to Meal Planner screen

fun NavController.navigateToMealPlanner() = navigate(route = MealPlannerRoute)

fun NavGraphBuilder.mealPlannerScreen() {
    composable<MealPlannerRoute> {
        MealPlannerScreen()
    }
}