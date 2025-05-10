package com.example.pantryplan.core.data.access.repository

import com.example.pantryplan.core.models.PantryItem
import com.example.pantryplan.core.models.PantryItemState
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface PantryItemRepository {
    suspend fun addItemToRepository(item: PantryItem)
    suspend fun removeItemFromRepository(item: PantryItem)

    fun getAllItems(): Flow<List<PantryItem>>
    fun getItemById(id: UUID): Flow<PantryItem?>
    fun getItemByBarcode(barcode: String): Flow<PantryItem?>

    suspend fun updateItem(item: PantryItem)
    suspend fun updateItemStateById(id: UUID, state: PantryItemState)

    fun searchForItemsByName(name: String): Flow<List<PantryItem>>
}