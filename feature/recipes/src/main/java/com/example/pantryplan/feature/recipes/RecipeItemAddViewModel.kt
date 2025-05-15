package com.example.pantryplan.feature.recipes

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.pantryplan.core.data.access.repository.PantryItemRepository
import com.example.pantryplan.core.data.access.repository.RecipeRepository
import com.example.pantryplan.core.models.Allergen
import com.example.pantryplan.core.models.Ingredient
import com.example.pantryplan.core.models.NutritionInfo
import com.example.pantryplan.core.models.Recipe
import com.example.pantryplan.feature.recipes.navigation.RecipeItemAdd
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import java.util.EnumSet
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class RecipeItemAddViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val recipeItemRepository: RecipeRepository,
    private val pantryItemRepository: PantryItemRepository
) : ViewModel() {
    private val existingId = savedStateHandle
        .toRoute<RecipeItemAdd>()
        .id?.let(UUID::fromString)

    private var recipeItem = Recipe(
        id = UUID.randomUUID(),
        title = "",
        description = "",
        tags = emptyList(),
        allergens = EnumSet.noneOf(Allergen::class.java),
        imageUrl = null,
        instructions = emptyList(),
        ingredients = emptyList(),
        prepTime = 0f,
        cookTime = 0f,
        nutrition = NutritionInfo(
            calories = 0,
            fats = 0f,
            saturatedFats = 0f,
            carbohydrates = 0f,
            sugar = 0f,
            fiber = 0f,
            protein = 0f,
            sodium = 0f
        ),
    )

    private val _uiState = MutableStateFlow(
        RecipeItemAddUiState(
            recipeItem = recipeItem,
            jsonForAllergenSet = Json.encodeToString(recipeItem),
        )
    )
    val uiState: StateFlow<RecipeItemAddUiState> = _uiState.asStateFlow()

    fun updateName(title: String) {
        recipeItem = recipeItem.copy(title = title)
        _uiState.update { it.copy(recipeItem = recipeItem) }
    }

    fun updateDescription(description: String) {
        recipeItem = recipeItem.copy(description = description)
        _uiState.update { it.copy(recipeItem = recipeItem) }
    }

    fun updateTags(tag: String) {
        recipeItem = recipeItem.copy(tags = recipeItem.tags.plus(tag))
        _uiState.update { it.copy(recipeItem = recipeItem) }
    }

    fun removeTags(tag: String) {
        recipeItem = recipeItem.copy(tags = recipeItem.tags.minus(tag))
        _uiState.update { it.copy(recipeItem = recipeItem) }
    }

    fun updateAllergens(allergenSet: EnumSet<Allergen>) {
        recipeItem = recipeItem.copy(allergens = allergenSet)

        _uiState.update {
            it.copy(
                recipeItem = recipeItem,
                jsonForAllergenSet = Json.encodeToString(recipeItem)
            )
        }
    }

    fun updateInstructions(instruction: String) {
        recipeItem = recipeItem.copy(instructions = recipeItem.instructions.plus(instruction))
        _uiState.update { it.copy(recipeItem = recipeItem) }
    }

    fun removeInstructions(instruction: String) {
        recipeItem = recipeItem.copy(instructions = recipeItem.instructions.minus(instruction))
        _uiState.update { it.copy(recipeItem = recipeItem) }
    }

    fun updateIngredients(ingredient: Ingredient) {
        viewModelScope.launch {
            val itemList = pantryItemRepository.searchForItemsByName(ingredient.name)
            val linkedPantryItem = itemList.firstOrNull()?.firstOrNull()

            recipeItem = recipeItem.copy(
                ingredients = recipeItem.ingredients.plus(
                    ingredient.copy(linkedPantryItem = linkedPantryItem)
                )
            )
            _uiState.update { it.copy(recipeItem = recipeItem) }
        }
    }

    fun removeIngredients(ingredient: Ingredient) {
        viewModelScope.launch {
            recipeItem = recipeItem.copy(ingredients = recipeItem.ingredients.minus(ingredient))
            _uiState.update { it.copy(recipeItem = recipeItem) }
        }
    }

    fun updatePrepTime(prepMins: Float) {
        recipeItem = recipeItem.copy(prepTime = prepMins)
        _uiState.update { it.copy(recipeItem = recipeItem) }
    }

    fun updateCookTime(cookMins: Float) {
        recipeItem = recipeItem.copy(cookTime = cookMins)
        _uiState.update { it.copy(recipeItem = recipeItem) }
    }

    fun updateNutritionalInfo(nutritionalInfo: NutritionInfo) {
        recipeItem = recipeItem.copy(nutrition = nutritionalInfo)
        _uiState.update { it.copy(recipeItem = recipeItem) }
    }

    fun saveRecipeItem() {
        viewModelScope.launch {
            val recipeItem = recipeItem.copy(
            )

            if (existingId == null) {
                recipeItemRepository.addRecipe(recipeItem)
            } else {
                recipeItemRepository.updateRecipe(recipeItem)
            }
        }
    }
}

data class RecipeItemAddUiState(
    val recipeItem: Recipe,
    val jsonForAllergenSet: String,
)