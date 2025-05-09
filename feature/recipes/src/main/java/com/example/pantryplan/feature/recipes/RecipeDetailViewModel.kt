package com.example.pantryplan.feature.recipes

import androidx.compose.runtime.mutableStateSetOf
import androidx.compose.runtime.snapshots.SnapshotStateSet
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pantryplan.core.data.access.repository.UserPreferencesRepository
import com.example.pantryplan.core.models.Allergen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class RecipeDetailViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {
    val uiState: StateFlow<RecipePreferencesUiState> = userPreferencesRepository.preferences
        .map { preferences ->
            val recipeAllergySet: SnapshotStateSet<Allergen> = mutableStateSetOf()
            recipeAllergySet.addAll(preferences.allergies)

            RecipePreferencesUiState(
                allergies = recipeAllergySet
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(1.seconds.inWholeMilliseconds),
            initialValue = RecipePreferencesUiState(),
        )

}

data class RecipePreferencesUiState(
    val allergies: SnapshotStateSet<Allergen> = mutableStateSetOf()
)
