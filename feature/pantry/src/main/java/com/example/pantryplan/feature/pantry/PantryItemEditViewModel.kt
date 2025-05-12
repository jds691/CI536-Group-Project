package com.example.pantryplan.feature.pantry

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pantryplan.core.data.access.repository.PantryItemRepository
import com.example.pantryplan.core.models.PantryItem
import com.example.pantryplan.core.models.PantryItemState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import java.util.UUID
import javax.inject.Inject
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class PantryItemEditViewModel @Inject constructor(
    private val pantryItemRepository: PantryItemRepository,
) : ViewModel() {
    private var pantryItem = MutableStateFlow(PantryItem(
        id = UUID.randomUUID(),
        name = "",
        quantity = 0,
        expiryDate = Clock.System.now() + 7.days,
        expiresAfter = Duration.ZERO,
        inStateSince = Clock.System.now(),
        state = PantryItemState.SEALED,
        imageUrl = null,
        barcode = null
    ))

    private var quantityUnit = MutableStateFlow(QuantityUnit.GRAMS)
    private var expiresAfterUnit = MutableStateFlow(ExpiresAfterUnit.DAYS)

    val uiState: StateFlow<PantryItemEditUiState> = combine(
        pantryItem,
        quantityUnit,
        expiresAfterUnit,
    ) { pantryItem, quantityUnit, expiresAfterUnit ->
        PantryItemEditUiState(pantryItem, quantityUnit, expiresAfterUnit)
    }.stateIn(
        scope = viewModelScope,
        started = WhileSubscribed(5.seconds.inWholeMilliseconds),
        PantryItemEditUiState(pantryItem.value, quantityUnit.value, expiresAfterUnit.value)
    )

    fun updateName(name: String) {
        pantryItem.value = pantryItem.value.copy(name = name)
    }

    fun updateExpiryDate(expiryDate: Instant) {
        pantryItem.value = pantryItem.value.copy(expiryDate = expiryDate)
    }

    fun updateState(state: PantryItemState) {
        pantryItem.value = pantryItem.value.copy(state = state)
    }

    fun updateQuantity(quantity: Int) {
        pantryItem.value = pantryItem.value.copy(quantity = quantity)
    }

    fun updateQuantityUnit(quantityUnit: QuantityUnit) {
        this.quantityUnit.value = quantityUnit
    }

    fun updateExpiresAfter(expiresAfter: Duration) {
        pantryItem.value = pantryItem.value.copy(expiresAfter = expiresAfter)
    }

    fun updateExpiresAfterUnit(expiresAfterUnit: ExpiresAfterUnit) {
        this.expiresAfterUnit.value = expiresAfterUnit
    }

    fun savePantryItem() {
        viewModelScope.launch {
            val newQuantity = pantryItem.value.quantity * when (quantityUnit.value) {
                QuantityUnit.GRAMS -> 1
                QuantityUnit.KILOGRAMS -> 1000
            }

            // TODO: Replace with .weeks and .months when type is changed to DatePeriod.
            val newExpiresAfter = pantryItem.value.expiresAfter!! * when (expiresAfterUnit.value) {
                ExpiresAfterUnit.DAYS -> 1
                ExpiresAfterUnit.WEEKS -> 7
                ExpiresAfterUnit.MONTHS -> 30
            }

            val pantryItem = pantryItem.value.copy(
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