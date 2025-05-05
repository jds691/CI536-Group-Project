package com.example.pantryplan.feature.recipes.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import com.example.pantryplan.feature.recipes.RecipeItemAddScreen
import com.example.pantryplan.feature.recipes.RecipesScreen
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data object Recipes // Route to Pantry screen

@Serializable
data object RecipesList // Route to the root list

@Serializable
data class RecipeItemDetails(val id: String) // Route to the details screen

@Serializable
data class RecipeItemAdd(val id: String?) // Route to edit an existing recipe or create a new one

fun NavController.navigateToPantry() = navigate(route = RecipesList)
fun NavController.navigateToPantryItem(id: UUID) =
    navigate(route = RecipeItemDetails(id = id.toString()))

fun NavController.navigateToPantryItemEdit(id: UUID?) =
    navigate(route = RecipeItemAdd(id = id?.toString()))

fun NavGraphBuilder.pantrySection(
    onRecipeItemClick: (UUID) -> Unit,
    onRecipeItemAdd: (UUID?) -> Unit,
    onBackClick: () -> Unit
) {
    navigation<Recipes>(startDestination = RecipesList) {
        composable<RecipesList> {
            RecipesScreen(
                onClickRecipeItem = onRecipeItemClick,
                onCreateRecipeItem = {
                    onRecipeItemAdd(null)
                }
            )
        }

        /*composable<RecipeItemDetails> { entry ->
            val itemDetails = entry.toRoute<RecipeItemDetails>()

            RecipeItemDetailsScreen(
                id = UUID.fromString(itemDetails.id),
                onBackClick = onBackClick,
                onEditItem = onRecipeItemAdd
            )
        }*/

        composable<RecipeItemAdd> { entry ->
            val itemEdit = entry.toRoute<RecipeItemAdd>()

            RecipeItemAddScreen(
                existingId = if (itemEdit.id != null) UUID.fromString(itemEdit.id) else null,
                onBackClick = onBackClick
            )
        }
    }
}