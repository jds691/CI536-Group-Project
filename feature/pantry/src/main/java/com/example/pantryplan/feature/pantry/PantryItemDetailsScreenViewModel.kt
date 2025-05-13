package com.example.pantryplan.feature.pantry

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pantryplan.core.data.access.repository.PantryItemRepository
import com.example.pantryplan.core.models.PantryItem
import com.example.pantryplan.core.models.PantryItemState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class PantryItemDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val pantryItemRepository: PantryItemRepository,
) : ViewModel() {

    private val id: UUID =
        UUID.fromString(savedStateHandle.get<String>("id")!!)

    private val pantryItem: MutableStateFlow<PantryItem> =
        MutableStateFlow(
            runBlocking {
                pantryItemRepository.getItemById(id).first()
                    ?: error("Pantry item with ID $id not found.")
            }
        )

    val item: StateFlow<PantryItem> = pantryItem

    fun loadItem() {
        viewModelScope.launch {
            pantryItemRepository.getItemById(id).first()?.let { updatedItem ->
                pantryItem.value = updatedItem
            }
        }
    }

    fun updateState(newState: PantryItemState) {
        pantryItem.update {
            it.copy(state = newState, inStateSince = Clock.System.now())
        }

        viewModelScope.launch {
            pantryItemRepository.updateItem(pantryItem.value)
        }
    }

    fun deleteItem() {
        viewModelScope.launch {
            pantryItemRepository.removeItemFromRepository(pantryItem.value)
        }
    }
}