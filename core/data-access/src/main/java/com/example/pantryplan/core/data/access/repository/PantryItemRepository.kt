package com.example.pantryplan.core.data.access.repository

import com.example.pantryplan.core.models.PantryItem
import com.example.pantryplan.core.models.PantryItemState
import java.util.UUID

interface PantryItemRepository {
    suspend fun addItemToRepository(item: PantryItem)
    suspend fun removeItemFromRepository(item: PantryItem)

    suspend fun getAllItems(): List<PantryItem>
    suspend fun getItemById(id: UUID): PantryItem?
    suspend fun getItemByBarcode(barcode: String): PantryItem?

    suspend fun updateItem(item: PantryItem)
    suspend fun updateItemStateById(id: UUID, state: PantryItemState)

    suspend fun searchForItemsByName(name: String): List<PantryItem>
}