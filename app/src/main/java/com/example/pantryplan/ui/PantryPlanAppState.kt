package com.example.pantryplan.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.pantryplan.feature.meals.navigation.navigateToMealPlanner
import com.example.pantryplan.feature.pantry.navigation.navigateToPantry
import com.example.pantryplan.feature.recipes.navigation.navigateToRecipeList
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
            TopLevelDestination.RECIPES -> navController.navigateToRecipeList()
            TopLevelDestination.MEAL_PLANNER -> navController.navigateToMealPlanner()
        }
    }

    val currentDestination: NavDestination?
        @Composable
        get() {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            /* Sometimes, switching between TopLevelDestinations causes the current back stack entry
             * to briefly read 'null' before actually changing to the new destination.
             * This causes the top bar to disappear for a few frames. To mitigate this, we fallback
             * to the previous back stack entry whenever the current entry is null.
             */
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