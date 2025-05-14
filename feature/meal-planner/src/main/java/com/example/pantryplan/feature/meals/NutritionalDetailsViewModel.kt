package com.example.pantryplan.feature.meals

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pantryplan.core.data.access.repository.NutritionRepository
import com.example.pantryplan.core.models.NutritionInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class NutritionalDetailsViewModel @Inject constructor(
    nutritionRepository: NutritionRepository
) : ViewModel() {
    val uiState: StateFlow<NutritionalDetailsUiState> = nutritionRepository
        .getNutritionForDate(
            Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        )
        .map {
            NutritionalDetailsUiState(
                nutrition = it
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(1.seconds.inWholeMilliseconds),
            initialValue = NutritionalDetailsUiState(),
        )
}

data class NutritionalDetailsUiState(
    val nutrition: NutritionInfo = NutritionInfo(
        calories = 0,
        fats = 0f,
        saturatedFats = 0f,
        carbohydrates = 0f,
        sugar = 0f,
        fiber = 0f,
        protein = 0f,
        sodium = 0f
    )
)