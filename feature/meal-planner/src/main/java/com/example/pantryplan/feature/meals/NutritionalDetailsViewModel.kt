package com.example.pantryplan.feature.meals

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pantryplan.core.models.NutritionInfo
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

class NutritionalDetailsViewModel @Inject constructor(

) : ViewModel() {
    val uiState: StateFlow<NutritionalDetailsUiState> = flow<NutritionalDetailsUiState> {

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