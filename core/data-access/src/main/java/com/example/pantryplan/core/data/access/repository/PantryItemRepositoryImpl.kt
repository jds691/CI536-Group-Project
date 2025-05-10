package com.example.pantryplan.core.data.access.repository

import com.example.pantryplan.core.database.dao.PantryDao
import com.example.pantryplan.core.database.dao.PantryStockStateUpdate
import com.example.pantryplan.core.database.model.PantryStock
import com.example.pantryplan.core.database.model.asExternalModel
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
        pantryDao.addItem(item.asEntity())
    }

    override suspend fun removeItemFromRepository(item: PantryItem) {
        pantryDao.removeItem(item.asEntity())
    }

    override suspend fun getAllItems(): List<PantryItem> {
        return pantryDao.showAll().map { it.asExternalModel() }
    }

    override suspend fun getItemById(id: UUID): PantryItem? {
        return pantryDao.searchById(id)?.asExternalModel()
    }

    override suspend fun getItemByBarcode(barcode: String): PantryItem? {
        TODO("Not yet implemented")
    }

    override suspend fun updateItem(item: PantryItem) {
        pantryDao.updateItem(item.asEntity())
    }

    override suspend fun updateItemStateById(id: UUID, state: PantryItemState) {
        pantryDao.updateItemState(
            PantryStockStateUpdate(
                itemID = id,
                itemState = state
            )
        )
    }

    override suspend fun searchForItemsByName(name: String): List<PantryItem> {
        // % signs included as wildcards
        return pantryDao.fuzzySearchByName("%$name%").map { it.asExternalModel() }
    }
}