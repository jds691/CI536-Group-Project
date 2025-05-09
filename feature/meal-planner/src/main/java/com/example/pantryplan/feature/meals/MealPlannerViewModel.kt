package com.example.pantryplan.feature.meals

import androidx.compose.runtime.mutableStateSetOf
import androidx.compose.runtime.snapshots.SnapshotStateSet
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pantryplan.core.data.access.repository.UserPreferencesRepository
import com.example.pantryplan.core.models.Allergen
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class MealPlannerViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {
    val uiState: StateFlow<MealPlannerUiState> = userPreferencesRepository.preferences
        .map { preferences ->
            val recipeAllergySet: SnapshotStateSet<Allergen> = mutableStateSetOf()
            recipeAllergySet.addAll(preferences.allergies)

            MealPlannerUiState(
                allergies = recipeAllergySet
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(1.seconds.inWholeMilliseconds),
            initialValue = MealPlannerUiState(),
        )

}

data class MealPlannerUiState(
    val allergies: SnapshotStateSet<Allergen> = mutableStateSetOf()
)
