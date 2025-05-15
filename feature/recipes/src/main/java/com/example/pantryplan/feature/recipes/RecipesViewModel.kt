package com.example.pantryplan.feature.recipes

import androidx.compose.runtime.mutableStateSetOf
import androidx.compose.runtime.snapshots.SnapshotStateSet
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pantryplan.core.data.access.repository.RecipeRepository
import com.example.pantryplan.core.data.access.repository.UserPreferencesRepository
import com.example.pantryplan.core.models.Allergen
import com.example.pantryplan.core.models.Recipe
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class RecipesViewModel @Inject constructor(
    private val recipesRepository: RecipeRepository,
    userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {
    val uiState: StateFlow<RecipeUiState> = recipeUiState(
        recipesRepository,
        userPreferencesRepository
    )
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(1.seconds.inWholeMilliseconds),
            initialValue = RecipeUiState(),
        )

    fun deleteRecipe(recipe: Recipe) {
        viewModelScope.launch {
            recipesRepository.removeItem(recipe)
        }
    }
}

private fun recipeUiState(
    recipesRepository: RecipeRepository,
    userPreferencesRepository: UserPreferencesRepository
): Flow<RecipeUiState> {
    val recipeItemsFlow = recipesRepository.getAllRecipes()
    val preferencesFlow = userPreferencesRepository.preferences

    return combine(recipeItemsFlow, preferencesFlow) { recipeItems, preferences ->
        val recipeAllergySet: SnapshotStateSet<Allergen> = mutableStateSetOf()
        recipeAllergySet.addAll(preferences.allergies)

        RecipeUiState(
            recipeItems = recipeItems,
            allergies = recipeAllergySet
        )
    }
}

data class RecipeUiState(
    val allergies: SnapshotStateSet<Allergen> = mutableStateSetOf(),
    val recipeItems: List<Recipe> = emptyList()
)
