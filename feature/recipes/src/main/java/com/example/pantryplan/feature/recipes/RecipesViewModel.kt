package com.example.pantryplan.feature.recipes

import androidx.lifecycle.ViewModel
import com.example.pantryplan.core.models.Recipe
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class RecipesViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(RecipeUiState())
    val uiState: StateFlow<RecipeUiState> = _uiState.asStateFlow()
}

data class RecipeUiState(val recipeItems: List<Recipe> = emptyList())