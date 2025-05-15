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
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import java.util.EnumSet
import java.util.UUID
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class RecipeItemAddViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val recipeItemRepository: RecipeRepository,
    private val pantryItemRepository: PantryItemRepository
) : ViewModel() {
    private val existingId = savedStateHandle
        .toRoute<RecipeItemAdd>()
        .id?.let(UUID::fromString)

    private var newRecipeItem = Recipe(
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

    private var recipeItem: MutableStateFlow<Recipe> =
        MutableStateFlow(
            if (existingId != null) {
                runBlocking { recipeItemRepository.getRecipeById(existingId).first()!! }
            } else {
                newRecipeItem
            }
        )



    private val _uiState = MutableStateFlow(
        RecipeItemAddUiState(
            recipeItem = recipeItem.value,
            jsonForAllergenSet = Json.encodeToString(recipeItem.value),
        )
    )

    val uiState = recipeItem.map {
        RecipeItemAddUiState(
            recipeItem = it,
            jsonForAllergenSet = Json.encodeToString(it)
        )
    }.stateIn(
        scope = viewModelScope,
        started = WhileSubscribed(5.seconds.inWholeMilliseconds),
        RecipeItemAddUiState(
            recipeItem.value,
            jsonForAllergenSet = Json.encodeToString(recipeItem.value)
        )
    )

    fun updateName(title: String) {
        recipeItem.update { it.copy(title = title) }
    }

    fun updateDescription(description: String) {
        recipeItem.update { it.copy(description = description) }
    }

    fun updateTags(tag: String) {
        recipeItem.update { it.copy(tags = it.tags.plus(tag)) }
    }

    fun removeTags(tag: String) {
        recipeItem.update { it.copy(tags = it.tags.minus(tag)) }
    }

    fun updateAllergens(allergenSet: EnumSet<Allergen>) {
        recipeItem.update { it.copy(allergens = allergenSet) }

        _uiState.update {
            it.copy(
                recipeItem = recipeItem.value,
                jsonForAllergenSet = Json.encodeToString(recipeItem.value)
            )
        }
    }

    fun updateInstructions(instruction: String) {
        recipeItem.update { it.copy(instructions = it.instructions.plus(instruction)) }
    }

    fun removeInstructions(instruction: String) {
        recipeItem.update { it.copy(instructions = it.instructions.minus(instruction)) }
    }

    fun updateIngredients(ingredient: Ingredient) {
        viewModelScope.launch {
            val itemList = pantryItemRepository.searchForItemsByName(ingredient.name)
            val linkedPantryItem = itemList.firstOrNull()?.firstOrNull()

            recipeItem.update {
                it.copy(
                    ingredients = it.ingredients.plus(
                        ingredient.copy(
                            linkedPantryItem = linkedPantryItem
                        )
                    )
                )
            }

            _uiState.update { it.copy(recipeItem = recipeItem.value) }
        }
    }

    fun removeIngredients(ingredient: Ingredient) {
        viewModelScope.launch {
            recipeItem.update { it.copy(ingredients = it.ingredients.minus(ingredient)) }
        }
    }

    fun updatePrepTime(prepMins: Float) {
        recipeItem.update { it.copy(prepTime = prepMins) }
    }

    fun updateCookTime(cookMins: Float) {
        recipeItem.update { it.copy(cookTime = cookMins) }
    }

    fun updateNutritionalInfo(nutritionalInfo: NutritionInfo) {
        recipeItem.update { it.copy(nutrition = nutritionalInfo) }
    }

    fun saveRecipeItem() {
        viewModelScope.launch {
            if (existingId == null) {
                recipeItemRepository.addRecipe(recipeItem.value)
            } else {
                recipeItemRepository.updateRecipe(recipeItem.value)
            }
        }
    }
}

data class RecipeItemAddUiState(
    val recipeItem: Recipe,
    val jsonForAllergenSet: String,
)