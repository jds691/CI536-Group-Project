package com.example.pantryplan.feature.pantry

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pantryplan.core.data.access.repository.PantryItemRepository
import com.example.pantryplan.core.models.PantryItem
import com.example.pantryplan.core.models.PantryItemState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import java.util.UUID
import javax.inject.Inject
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days

@HiltViewModel
class PantryItemEditViewModel @Inject constructor(
    private val pantryItemRepository: PantryItemRepository,
) : ViewModel() {
    private var pantryItem = PantryItem(
        id = UUID.randomUUID(),
        name = "",
        quantity = 0,
        expiryDate = Clock.System.now() + 7.days,
        expiresAfter = Duration.ZERO,
        inStateSince = Clock.System.now(),
        state = PantryItemState.SEALED,
        imageUrl = null,
        barcode = null
    )

    private val _uiState = MutableStateFlow(PantryItemEditUiState(pantryItem))
    val uiState: StateFlow<PantryItemEditUiState> = _uiState.asStateFlow()

    fun updateName(name: String) {
        viewModelScope.launch {
            pantryItem = pantryItem.copy(name = name)
            _uiState.update { it.copy(pantryItem = pantryItem) }
        }
    }

    fun updateExpiryDate(expiryDate: Instant) {
        viewModelScope.launch {
            pantryItem = pantryItem.copy(expiryDate = expiryDate)
            _uiState.update { it.copy(pantryItem = pantryItem) }
        }
    }

    fun updateState(state: PantryItemState) {
        viewModelScope.launch {
            pantryItem = pantryItem.copy(state = state)
            _uiState.update { it.copy(pantryItem = pantryItem) }
        }
    }

    fun updateQuantity(quantity: Int) {
        viewModelScope.launch {
            pantryItem = pantryItem.copy(quantity = quantity)
            _uiState.update { it.copy(pantryItem = pantryItem) }
        }
    }

    fun updateExpiresAfter(expiresAfter: Duration) {
        viewModelScope.launch {
            pantryItem = pantryItem.copy(expiresAfter = expiresAfter)
            _uiState.update { it.copy(pantryItem = pantryItem) }
        }
    }

    fun savePantryItem() {
        viewModelScope.launch {
            pantryItemRepository.addItemToRepository(pantryItem)
        }
    }
}

data class PantryItemEditUiState(
    val pantryItem: PantryItem
)