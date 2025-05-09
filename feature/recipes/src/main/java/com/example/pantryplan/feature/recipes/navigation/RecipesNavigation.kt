package com.example.pantryplan.feature.recipes.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import com.example.pantryplan.core.models.Allergen
import com.example.pantryplan.core.models.NutritionInfo
import com.example.pantryplan.core.models.Recipe
import com.example.pantryplan.feature.recipes.RecipeItemAddScreen
import com.example.pantryplan.feature.recipes.RecipeItemDetailsScreen
import com.example.pantryplan.feature.recipes.RecipesScreen
import kotlinx.serialization.Serializable
import java.util.EnumSet
import java.util.UUID

@Serializable
data object Recipes // Route to Pantry screen

@Serializable
data object RecipesList // Route to the root list

@Serializable
data class RecipeItemDetails(val id: String) // Route to the details screen

@Serializable
data class RecipeItemAdd(val id: String?) // Route to edit an existing recipe or create a new one

fun NavController.navigateToRecipeList() = navigate(route = RecipesList)
fun NavController.navigateToRecipeItem(id: UUID) =
    navigate(route = RecipeItemDetails(id = id.toString()))

fun NavController.navigateToRecipeItemAdd(id: UUID?) =
    navigate(route = RecipeItemAdd(id = id?.toString()))

fun NavGraphBuilder.recipeSection(
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

        composable<RecipeItemDetails> { entry ->
            val itemDetails = entry.toRoute<RecipeItemDetails>()
            val recipe = Recipe(
                id = UUID.randomUUID(),
                title = "Cheeseburger",
                description = "Burger packed with juicy beef, melted cheese and extra vegetables to add that final flavour.",
                tags = listOf("Dinner", "High Protein"),
                allergens = EnumSet.of(Allergen.MILK, Allergen.GLUTEN, Allergen.SESAME),
                imageUrl = null,
                instructions = listOf("Cook Burger", "Eat burger"),
                ingredients = listOf(
                    "Beef Burger",
                    "Burger Buns",
                    "American Cheese",
                    "Lettuce",
                    "Red Onion",
                    "Bacon"
                ),
                prepTime = 10f,
                cookTime = 15f,
                nutrition = NutritionInfo(
                    calories = 100,
                    fats = 100f,
                    saturatedFats = 100f,
                    carbohydrates = 100f,
                    sugar = 100f,
                    fiber = 100f,
                    protein = 100f,
                    sodium = 100f
                )
            )
            RecipeItemDetailsScreen(
                item = recipe,
                id = UUID.fromString(itemDetails.id),
                onBackClick = onBackClick,
                onEditItem = onRecipeItemAdd
            )
        }

        composable<RecipeItemAdd> { entry ->
            val itemEdit = entry.toRoute<RecipeItemAdd>()

            RecipeItemAddScreen(
                existingId = if (itemEdit.id != null) UUID.fromString(itemEdit.id) else null,
                onBackClick = onBackClick
            )
        }
    }
}