package com.example.pantryplan.feature.pantry.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.pantryplan.feature.pantry.PantryItemDetailsScreen
import com.example.pantryplan.feature.pantry.PantryScreen
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data object Pantry // Route to Pantry screen
@Serializable
data object PantryList
@Serializable
data class PantryItemDetails(val id: String)


fun NavController.navigateToPantry() = navigate(route = PantryList)
fun NavController.navigateToPantryItem(id: UUID) =
    navigate(route = PantryItemDetails(id = id.toString()))

fun NavGraphBuilder.pantrySection(
    onPantryItemClick: (UUID) -> Unit,
    onBackClick: () -> Unit
) {
    navigation<Pantry>(startDestination = PantryList) {
        composable<PantryList> {
            PantryScreen(
                onClickPantryItem = onPantryItemClick
            )
        }

        composable<PantryItemDetails>(
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Start
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Start
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.End
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.End
                )
            }
        ) {
            PantryItemDetailsScreen(
                onBackClick = onBackClick
            )
        }
    }
}