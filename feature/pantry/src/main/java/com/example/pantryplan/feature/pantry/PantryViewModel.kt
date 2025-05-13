package com.example.pantryplan.feature.pantry

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pantryplan.core.data.access.repository.PantryItemRepository
import com.example.pantryplan.core.models.PantryItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class PantryViewModel @Inject constructor(
    private val pantryItemRepository: PantryItemRepository,
) : ViewModel() {
    val uiState: StateFlow<PantryUiState> = pantryItemRepository
        .getAllItems()
        .map { PantryUiState(it) }
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5.seconds.inWholeMilliseconds),
            initialValue = PantryUiState(emptyList()),
        )

    fun deletePantryItem(pantryItem: PantryItem) {
        viewModelScope.launch {
            pantryItemRepository.removeItemFromRepository(pantryItem)
        }
    }
}

data class PantryUiState(val pantryItems: List<PantryItem>)