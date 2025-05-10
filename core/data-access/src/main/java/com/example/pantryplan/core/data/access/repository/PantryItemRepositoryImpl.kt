package com.example.pantryplan.core.data.access.repository

import com.example.pantryplan.core.database.dao.PantryDao
import com.example.pantryplan.core.database.model.PantryStock
import com.example.pantryplan.core.models.PantryItem
import com.example.pantryplan.core.models.PantryItemState
import java.util.UUID
import javax.inject.Inject

fun PantryItem.asEntity() = PantryStock(
    itemID = id,
    itemName = name,
    dateExpiring = expiryDate,
    expiresAfter = expiresAfter,
    quantity = quantity,
    inStateSince = inStateSince,
    itemState = state,
    imageRefURL = imageUrl
)

class PantryItemRepositoryImpl @Inject constructor(
    private val pantryDao: PantryDao
) : PantryItemRepository {
    override suspend fun addItemToRepository(item: PantryItem) {
        TODO("Not yet implemented")
    }

    override suspend fun removeItemFromRepository(item: PantryItem) {
        TODO("Not yet implemented")
    }

    override suspend fun getAllItems(): List<PantryItem> {
        TODO("Not yet implemented")
    }

    override suspend fun getItemById(id: UUID): PantryItem? {
        TODO("Not yet implemented")
    }

    override suspend fun getItemByBarcode(barcode: String): PantryItem? {
        TODO("Not yet implemented")
    }

    override suspend fun updateItem(item: PantryItem) {
        TODO("Not yet implemented")
    }

    override suspend fun updateItemStateById(id: UUID, state: PantryItemState) {
        TODO("Not yet implemented")
    }

    override suspend fun searchForItemsByName(name: String): List<PantryItem> {
        TODO("Not yet implemented")
    }
}