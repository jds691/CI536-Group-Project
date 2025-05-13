package com.example.pantryplan.feature.meals

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.mutableStateSetOf
import androidx.compose.runtime.snapshots.SnapshotStateSet
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pantryplan.core.data.access.repository.UserPreferencesRepository
import com.example.pantryplan.core.models.Allergen
import com.example.pantryplan.core.models.NutritionInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class MealPlannerViewModel @Inject constructor(
    userPreferencesRepository: UserPreferencesRepository
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
    val dailyNutrition: MutableState<NutritionInfo> = mutableStateOf(
        NutritionInfo(
            calories = 0,
            fats = 0f,
            saturatedFats = 0f,
            carbohydrates = 0f,
            sugar = 0f,
            fiber = 0f,
            protein = 0f,
            sodium = 0f
        )
    ),
    val allergies: SnapshotStateSet<Allergen> = mutableStateSetOf()
)
