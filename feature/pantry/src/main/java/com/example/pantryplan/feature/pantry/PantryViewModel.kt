package com.example.pantryplan.feature.pantry

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pantryplan.core.data.access.repository.PantryItemRepository
import com.example.pantryplan.core.data.access.repository.UserPreferencesRepository
import com.example.pantryplan.core.datastore.UserPreferences
import com.example.pantryplan.core.models.PantryItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class PantryViewModel @Inject constructor(
    private val pantryItemRepository: PantryItemRepository,
    userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {
    val uiState: StateFlow<PantryUiState> = pantryUiState(
        pantryItemRepository,
        userPreferencesRepository
    )
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5.seconds.inWholeMilliseconds),
            initialValue = PantryUiState(emptyList(), 2.days),
        )

    fun deletePantryItem(pantryItem: PantryItem) {
        viewModelScope.launch {
            pantryItemRepository.removeItemFromRepository(pantryItem)
        }
    }
}

private fun pantryUiState(
    pantryItemRepository: PantryItemRepository,
    userPreferencesRepository: UserPreferencesRepository
): Flow<PantryUiState> {
    val pantryItemsFlow: Flow<List<PantryItem>> = pantryItemRepository.getAllItems()

    val preferencesFlow: Flow<UserPreferences> = userPreferencesRepository.preferences

    return combine(pantryItemsFlow, preferencesFlow, ::Pair).map { (pantryItems, preferences) ->
        PantryUiState(
            pantryItems = pantryItems,
            expiringSoonDuration = preferences.expiringSoonAmount
        )
    }
}

data class PantryUiState(val pantryItems: List<PantryItem>, val expiringSoonDuration: Duration)