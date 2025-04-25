package com.example.pantryplan.ui

import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.NavDestination.Companion.hasRoute
import com.example.pantryplan.feature.meals.navigation.navigateToMealPlanner
import com.example.pantryplan.feature.pantry.navigation.navigateToPantry
import com.example.pantryplan.feature.recipes.navigation.navigateToRecipes
import com.example.pantryplan.navigation.TopLevelDestination

@Composable
fun rememberPantryPlanAppState(
    navController: NavHostController = rememberNavController()
): PantryPlanAppState {
    return remember(navController) {
        PantryPlanAppState (navController)
    }
}

@Stable
class PantryPlanAppState (
    val navController: NavHostController
) {
    fun navigateToTopLevelDestination(topLevelDestination: TopLevelDestination) {
        when (topLevelDestination) {
            TopLevelDestination.PANTRY -> navController.navigateToPantry()
            TopLevelDestination.RECIPES -> navController.navigateToRecipes()
            TopLevelDestination.MEAL_PLANNER -> navController.navigateToMealPlanner()
        }
    }

    val currentDestination: NavDestination?
        @Composable
        get() {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            navBackStackEntry?.let { return it.destination }
            return navController.previousBackStackEntry?.destination
        }

    val currentTopLevelDestination: TopLevelDestination?
        @Composable
        get() {
            return TopLevelDestination.entries.firstOrNull { topLevelDestination ->
                navController.currentDestination?.hasRoute(topLevelDestination.route) == true
            }
        }
}