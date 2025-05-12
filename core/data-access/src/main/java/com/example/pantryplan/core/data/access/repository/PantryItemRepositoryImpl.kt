package com.example.pantryplan.core.data.access.repository

import com.example.pantryplan.core.database.dao.PantryDao
import com.example.pantryplan.core.database.dao.PantryStockStateUpdate
import com.example.pantryplan.core.database.model.PantryStock
import com.example.pantryplan.core.database.model.asExternalModel
import com.example.pantryplan.core.models.PantryItem
import com.example.pantryplan.core.models.PantryItemState
import com.example.pantryplan.core.notifications.Notifier
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
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
    imageRefURL = imageUrl,
    barcode = barcode
)

class PantryItemRepositoryImpl @Inject constructor(
    private val pantryDao: PantryDao,
    private val notifier: Notifier
) : PantryItemRepository {
    override suspend fun addItemToRepository(item: PantryItem) {
        pantryDao.addItem(item.asEntity())
        notifier.scheduleNotificationsForPantryItems(
            listOf(
                item
            )
        )
    }

    override suspend fun removeItemFromRepository(item: PantryItem) {
        pantryDao.removeItem(item.asEntity())
        notifier.cancelNotificationsForPantryItems(
            listOf(
                item
            )
        )
    }

    override fun getAllItems(): Flow<List<PantryItem>> {
        return pantryDao.showAll().map { it.map(PantryStock::asExternalModel) }
    }

    override fun getItemById(id: UUID): Flow<PantryItem?> {
        return pantryDao.searchDistinctByIdUntilChanged(id).map { it?.asExternalModel() }
    }

    override fun getItemByBarcode(barcode: String): Flow<PantryItem?> {
        return pantryDao.getDistinctStockByBarcodeUntilChanged(barcode)
            .map { it?.asExternalModel() }
    }

    override suspend fun updateItem(item: PantryItem) {
        pantryDao.updateItem(item.asEntity())
        notifier.scheduleNotificationsForPantryItems(
            listOf(
                item
            )
        )
    }

    override suspend fun updateItemStateById(id: UUID, state: PantryItemState) {
        pantryDao.updateItemState(
            PantryStockStateUpdate(
                itemID = id,
                itemState = state
            )
        )

        val item = pantryDao.searchById(id).first() ?: return

        notifier.scheduleNotificationsForPantryItems(
            listOf(
                item.asExternalModel()
            )
        )
    }

    override fun searchForItemsByName(name: String): Flow<List<PantryItem>> {
        // % signs included as wildcards
        return pantryDao.fuzzySearchByName("%$name%").map { it.map(PantryStock::asExternalModel) }
    }
}