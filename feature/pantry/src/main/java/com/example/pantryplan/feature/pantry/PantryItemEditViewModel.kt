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

    private var quantityUnit = QuantityUnit.GRAMS
    private var expiresAfterUnit = ExpiresAfterUnit.DAYS

    private val _uiState = MutableStateFlow(
        PantryItemEditUiState(
            pantryItem = pantryItem,
            quantityUnit = quantityUnit,
            expiresAfterUnit = expiresAfterUnit,
        )
    )
    val uiState: StateFlow<PantryItemEditUiState> = _uiState.asStateFlow()

    fun updateName(name: String) {
        pantryItem = pantryItem.copy(name = name)
        _uiState.update { it.copy(pantryItem = pantryItem) }
    }

    fun updateExpiryDate(expiryDate: Instant) {
        pantryItem = pantryItem.copy(expiryDate = expiryDate)
        _uiState.update { it.copy(pantryItem = pantryItem) }
    }

    fun updateState(state: PantryItemState) {
        pantryItem = pantryItem.copy(state = state)
        _uiState.update { it.copy(pantryItem = pantryItem) }
    }

    fun updateQuantity(quantity: Int) {
        pantryItem = pantryItem.copy(quantity = quantity)
        _uiState.update { it.copy(pantryItem = pantryItem) }
    }

    fun updateQuantityUnit(quantityUnit: QuantityUnit) {
        this.quantityUnit = quantityUnit
        _uiState.update { it.copy(quantityUnit = quantityUnit) }
    }

    fun updateExpiresAfter(expiresAfter: Duration) {
        pantryItem = pantryItem.copy(expiresAfter = expiresAfter)
        _uiState.update { it.copy(pantryItem = pantryItem) }
    }

    fun updateExpiresAfterUnit(expiresAfterUnit: ExpiresAfterUnit) {
        this.expiresAfterUnit = expiresAfterUnit
        _uiState.update { it.copy(expiresAfterUnit = expiresAfterUnit) }
    }

    fun savePantryItem() {
        viewModelScope.launch {
            val newQuantity = pantryItem.quantity * when (quantityUnit) {
                QuantityUnit.GRAMS -> 1
                QuantityUnit.KILOGRAMS -> 1000
            }

            // TODO: Replace with .weeks and .months when type is changed to DatePeriod.
            val newExpiresAfter = pantryItem.expiresAfter!! * when (expiresAfterUnit) {
                ExpiresAfterUnit.DAYS -> 1
                ExpiresAfterUnit.WEEKS -> 7
                ExpiresAfterUnit.MONTHS -> 30
            }

            val pantryItem = pantryItem.copy(
                quantity = newQuantity,
                expiresAfter = newExpiresAfter,
            )
            pantryItemRepository.addItemToRepository(pantryItem)
        }
    }
}

data class PantryItemEditUiState(
    val pantryItem: PantryItem,
    val quantityUnit: QuantityUnit,
    val expiresAfterUnit: ExpiresAfterUnit,
)

enum class QuantityUnit {
    GRAMS, KILOGRAMS
}

enum class ExpiresAfterUnit {
    DAYS, WEEKS, MONTHS
}