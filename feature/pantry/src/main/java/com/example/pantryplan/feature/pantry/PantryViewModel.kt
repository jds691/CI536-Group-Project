package com.example.pantryplan.feature.pantry

import androidx.lifecycle.ViewModel
import com.example.pantryplan.core.models.PantryItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class PantryViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(PantryUiState())
    val uiState: StateFlow<PantryUiState> = _uiState.asStateFlow()
}

data class PantryUiState(val pantryItems: List<PantryItem> = emptyList())