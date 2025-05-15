package com.example.pantryplan.feature.pantry

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.pantryplan.core.data.access.repository.PantryItemRepository
import com.example.pantryplan.core.models.Measurement
import com.example.pantryplan.core.models.PantryItem
import com.example.pantryplan.core.models.PantryItemState
import com.example.pantryplan.feature.pantry.navigation.PantryItemEdit
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import java.util.UUID
import javax.inject.Inject
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class PantryItemEditViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val pantryItemRepository: PantryItemRepository,
) : ViewModel() {
    private val existingId = savedStateHandle
        .toRoute<PantryItemEdit>()
        .id?.let(UUID::fromString)

    private val barcode = savedStateHandle.toRoute<PantryItemEdit>().barcode

    private val newPantryItem = PantryItem(
        id = UUID.randomUUID(),
        name = "",
        quantity = 0f,
        expiryDate = Clock.System.now() + 7.days,
        expiresAfter = Duration.ZERO,
        inStateSince = Clock.System.now(),
        state = PantryItemState.SEALED,
        imageUrl = null,
        barcode = barcode,
        measurement = Measurement.GRAMS
    )

    private var pantryItem: MutableStateFlow<PantryItem> =
        MutableStateFlow(
            if (existingId != null) {
                runBlocking { pantryItemRepository.getItemById(existingId).first()!! }
            } else {
                barcode?.let {
                    runBlocking {
                        pantryItemRepository.getItemByBarcode(barcode).first()
                    }?.copy(id = UUID.randomUUID())
                } ?: newPantryItem
            }
        )

    private var expiresAfterUnit = MutableStateFlow(ExpiresAfterUnit.DAYS)

    val uiState: StateFlow<PantryItemEditUiState> = combine(
        pantryItem,
        expiresAfterUnit,
    ) { pantryItem, expiresAfterUnit ->
        PantryItemEditUiState(pantryItem, expiresAfterUnit)
    }.stateIn(
        scope = viewModelScope,
        started = WhileSubscribed(5.seconds.inWholeMilliseconds),
        PantryItemEditUiState(pantryItem.value, expiresAfterUnit.value)
    )

    fun updateImageUri(imageUri: String?) {
        pantryItem.update { it.copy(imageUrl = imageUri) }
    }

    fun updateName(name: String) {
        pantryItem.update { it.copy(name = name) }
    }

    fun updateExpiryDate(expiryDate: Instant) {
        pantryItem.update { it.copy(expiryDate = expiryDate) }
    }

    fun updateState(state: PantryItemState) {
        pantryItem.update { it.copy(state = state) }
    }

    fun updateQuantity(quantity: Float) {
        pantryItem.update { it.copy(quantity = quantity) }
    }

    fun updateMeasurementUnit(measurementUnit: Measurement) {
        pantryItem.update { it.copy(measurement = measurementUnit) }
    }

    fun updateExpiresAfter(expiresAfter: Duration) {
        pantryItem.update { it.copy(expiresAfter = expiresAfter) }
    }

    fun updateExpiresAfterUnit(expiresAfterUnit: ExpiresAfterUnit) {
        this.expiresAfterUnit.update { expiresAfterUnit }
    }

    fun savePantryItem() {
        viewModelScope.launch {

            // TODO: Replace with .weeks and .months when type is changed to DatePeriod.
            val newExpiresAfter = pantryItem.value.expiresAfter!! * when (expiresAfterUnit.value) {
                ExpiresAfterUnit.DAYS -> 1
                ExpiresAfterUnit.WEEKS -> 7
                ExpiresAfterUnit.MONTHS -> 30
            }

            val pantryItem = pantryItem.value.copy(
                expiresAfter = newExpiresAfter,
            )

            if (existingId == null) {
                pantryItemRepository.addItemToRepository(pantryItem)
            } else {
                pantryItemRepository.updateItem(pantryItem)
            }
        }
    }
}

data class PantryItemEditUiState(
    val pantryItem: PantryItem,
    val expiresAfterUnit: ExpiresAfterUnit,
)

enum class ExpiresAfterUnit {
    DAYS, WEEKS, MONTHS
}