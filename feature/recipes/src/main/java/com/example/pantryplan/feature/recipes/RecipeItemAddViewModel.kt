package com.example.pantryplan.feature.recipes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pantryplan.core.models.Allergen
import com.example.pantryplan.core.models.NutritionInfo
import com.example.pantryplan.core.models.Recipe
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.EnumSet
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class RecipeItemAddViewModel @Inject constructor(
    //private val recipeItemRepository: RecipeItemRepository,
) : ViewModel() {
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

    //private var quantityUnit = QuantityUnit.GRAMS

    private val _uiState = MutableStateFlow(
        RecipeItemAddUiState(
            recipeItem = recipeItem,
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

    fun updateAllergens(allergen: Allergen) {
        val allergenSet = EnumSet.noneOf(Allergen::class.java)
        allergenSet.addAll(recipeItem.allergens)
        allergenSet.add(allergen)
        recipeItem = recipeItem.copy(allergens = allergenSet)
        _uiState.update { it.copy(recipeItem = recipeItem) }
    }

    fun updateInstructions(instruction: String) {
        recipeItem = recipeItem.copy(instructions = recipeItem.instructions.plus(instruction))
        _uiState.update { it.copy(recipeItem = recipeItem) }
    }

    /* TODO: Update ingredients when more are added
    fun updateIngredients(ingredient: )
     */

    fun updatePrepTime(prepMins: Float) {
        recipeItem = recipeItem.copy(prepTime = prepMins)
        _uiState.update { it.copy(recipeItem = recipeItem) }
    }

    fun updateCookTime(cookMins: Float) {
        recipeItem = recipeItem.copy(cookTime = cookMins)
        _uiState.update { it.copy(recipeItem = recipeItem) }
    }

    /* TODO: Update ingredients when more are added
    fun updateNutritionalInformation(nutritionalInfo: )
     */

    fun saveRecipeItem() {
        viewModelScope.launch {
            val recipeItem = recipeItem.copy(
            )
            //recipeItemRepository.addItemToRepository(recipeItem)
        }
    }
}

data class RecipeItemAddUiState(
    val recipeItem: Recipe,
)

enum class QuantityUnit {
    GRAMS, KILOGRAMS
}
