package com.example.pantryplan.feature.recipes

import androidx.compose.runtime.mutableStateSetOf
import androidx.compose.runtime.snapshots.SnapshotStateSet
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.pantryplan.core.data.access.repository.NutritionRepository
import com.example.pantryplan.core.data.access.repository.PantryItemRepository
import com.example.pantryplan.core.data.access.repository.RecipeRepository
import com.example.pantryplan.core.data.access.repository.UserPreferencesRepository
import com.example.pantryplan.core.models.Allergen
import com.example.pantryplan.core.models.NutritionInfo
import com.example.pantryplan.core.models.Recipe
import com.example.pantryplan.feature.recipes.navigation.RecipeItemDetails
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.EnumSet
import java.util.UUID
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class RecipeDetailViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val recipeItemRepository: RecipeRepository,
    private val pantryItemRepository: PantryItemRepository,
    private val nutritionRepository: NutritionRepository
) : ViewModel() {
    private val recipeId = savedStateHandle
        .toRoute<RecipeItemDetails>()
        .id.let(UUID::fromString)

    private val recipe = recipeItemRepository.getRecipeById(recipeId)
    private val preferences = userPreferencesRepository.preferences

    private val _servingAmount = MutableStateFlow(1)
    var servingAmount = _servingAmount.asStateFlow()

    fun changeServingAmount(serving: Int) {
        _servingAmount.value = serving
    }

    val uiState: StateFlow<RecipeDetailsUiState> = combine(
        recipe,
        preferences
    ) { recipe, preferences ->
        if (recipe == null) {
            RecipeDetailsUiState(
                recipe = placeholderMeal,
                allergies = mutableStateSetOf()
            )
        } else {
            val linkedIngredients = runBlocking {
                recipe.ingredients.map {
                    val linkedPantryItem = pantryItemRepository.searchForItemsByName(it.name)
                        .firstOrNull()
                        ?.firstOrNull()
                    it.copy(linkedPantryItem = linkedPantryItem)
                }
            }
            val linkedRecipe = recipe.copy(ingredients = linkedIngredients)

            val recipeAllergySet: SnapshotStateSet<Allergen> = mutableStateSetOf()
            recipeAllergySet.addAll(preferences.allergies)
            RecipeDetailsUiState(
                recipe = linkedRecipe,
                allergies = recipeAllergySet
            )
        }

    }
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5.seconds.inWholeMilliseconds),
            initialValue = RecipeDetailsUiState(recipe = placeholderMeal),
        )

    fun deleteRecipe(recipe: Recipe) {
        viewModelScope.launch {
            recipeItemRepository.removeItem(recipe)
        }
    }

    fun logRecipe(recipe: Recipe) {
        viewModelScope.launch {
            nutritionRepository.logNutrients(recipe)
        }
    }
}

data class RecipeDetailsUiState(
    val recipe: Recipe,
    val allergies: SnapshotStateSet<Allergen> = mutableStateSetOf(),
)

// TODO: Get rid of this placeholder by adding a loading state to RecipeDetailsUiState.
val placeholderMeal = Recipe(
    id = UUID.randomUUID(),
    title = "The Dumb Stupid Placeholder Meal",
    description = "Not tasty. Your real meal is loading.",
    tags = listOf(),
    allergens = EnumSet.of(Allergen.EGGS),
    imageUrl = null,
    instructions = listOf(),
    ingredients = listOf(),
    prepTime = 0.0f,
    cookTime = 0.0f,
    nutrition = NutritionInfo(
        calories = 0,
        fats = 0.0f,
        saturatedFats = 0.0f,
        carbohydrates = 0.0f,
        sugar = 0.0f,
        fiber = 0.0f,
        protein = 0.0f,
        sodium = 0.0f
    )
)
