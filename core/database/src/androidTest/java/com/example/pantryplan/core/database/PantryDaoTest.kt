package com.example.pantryplan.core.database

import com.example.pantryplan.core.database.dao.PantryStockStateUpdate
import com.example.pantryplan.core.database.model.PantryStock
import com.example.pantryplan.core.models.Measurement
import com.example.pantryplan.core.models.PantryItemState
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Clock
import org.junit.Test
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.time.Duration.Companion.days

internal class PantryDaoTest : DatabaseTest() {
    @Test
    fun getAllItems_And_addItem() = runTest {
        val firstItem = testPantryStock(
            name = "Test"
        )
        val secondItem = testPantryStock(
            name = "Test 2"
        )

        pantryDao.addItem(firstItem)
        pantryDao.addItem(secondItem)

        val items = pantryDao.showAll().first()

        assertEquals(items.size, 2)

        assertEquals(secondItem.itemID, items[1].itemID)
    }

    @Test
    fun getItemById() = runTest {
        val item = testPantryStock(
            name = "Test"
        )

        pantryDao.addItem(item)
        val searchedItem = pantryDao.searchById(item.itemID).first()

        // Checking the entire object fails due to floating point errors on the Instants
        assertEquals(item.itemName, searchedItem!!.itemName)
        assertEquals(item.itemState, searchedItem.itemState)

        assertNull(pantryDao.searchById(UUID.randomUUID()).first())
    }

    @Test
    fun getItemByBarcode() = runTest {
        val barcode = "123456789"
        val item = testPantryStock(
            name = "Barcode Item",
            barcode = barcode
        )

        pantryDao.addItem(item)

        val barcodeItem = pantryDao.getStockByBarcode(barcode).first()
        assertNotNull(barcodeItem)
        assertEquals(barcode, barcodeItem.barcode!!)
    }

    @Test
    fun fuzzySearchByName() = runTest {
        val milk = testPantryStock(
            name = "Milk"
        )

        val fuckedMilk = testPantryStock(
            name = "MiLk"
        )

        val coconutMilk = testPantryStock(
            name = "Coconut Milk"
        )

        val borger = testPantryStock(
            name = "Borger"
        )

        pantryDao.addItem(milk)
        pantryDao.addItem(fuckedMilk)
        pantryDao.addItem(coconutMilk)
        pantryDao.addItem(borger)

        val items = pantryDao.fuzzySearchByName("%Milk%").first()

        assertEquals(3, items.size)
        assertFalse(items.contains(borger))
    }

    @Test
    fun getItemsByState() = runTest {
        val frozen = testPantryStock(
            name = "Frozen",
            state = PantryItemState.FROZEN
        )

        val frozen2 = testPantryStock(
            name = "Frozen 2",
            state = PantryItemState.FROZEN
        )

        val expired = testPantryStock(
            name = "Expired",
            state = PantryItemState.EXPIRED
        )

        pantryDao.addItem(frozen)
        pantryDao.addItem(frozen2)
        pantryDao.addItem(expired)

        val items = pantryDao.getItemsByState(PantryItemState.FROZEN).first()

        assertEquals(2, items.size)
        assertEquals(frozen.itemName, items[0].itemName)
    }

    @Test
    fun updateItem() = runTest {
        val item = testPantryStock(
            name = "OG"
        )

        pantryDao.addItem(item)

        val newItem = testPantryStock(
            id = item.itemID,
            name = "N"
        )

        pantryDao.updateItem(newItem)

        val updatedItem = pantryDao.searchById(newItem.itemID).first()

        assertEquals(newItem.itemName, updatedItem!!.itemName)
    }

    @Test
    fun updateItemState() = runTest {
        val item = testPantryStock(
            name = "Frozen",
            state = PantryItemState.FROZEN
        )

        pantryDao.addItem(item)

        pantryDao.updateItemState(
            PantryStockStateUpdate(
                itemID = item.itemID,
                itemState = PantryItemState.OPENED
            )
        )

        val newItem = pantryDao.searchById(item.itemID).first()

        assertEquals(PantryItemState.OPENED, newItem!!.itemState)
    }

    @Test
    fun removeItem() = runTest {
        val item = testPantryStock(
            name = "Marked for Death"
        )

        pantryDao.addItem(item)

        val foundItem = pantryDao.searchById(item.itemID).first()
        assertNotNull(foundItem)

        pantryDao.removeItem(item)
        val foundItemAgain = pantryDao.searchById(item.itemID).first()
        assertNull(foundItemAgain)
    }

    @Test
    fun removeItemById() = runTest {
        val item = testPantryStock(
            name = "Marked for Death"
        )

        pantryDao.addItem(item)

        val foundItem = pantryDao.searchById(item.itemID).first()
        assertNotNull(foundItem)

        pantryDao.removeItemById(item.itemID)
        val foundItemAgain = pantryDao.searchById(item.itemID).first()
        assertNull(foundItemAgain)
    }
}

private fun testPantryStock(
    id: UUID = UUID.randomUUID(),
    name: String,
    state: PantryItemState = PantryItemState.OPENED,
    barcode: String? = null
) = PantryStock(
    itemID = id,
    itemName = name,
    dateExpiring = Clock.System.now().plus(7.days),
    expiresAfter = 2.days,
    quantity = 0f,
    inStateSince = Clock.System.now(),
    itemState = state,
    imageRefURL = null,
    barcode = barcode,
    measurement = Measurement.GRAMS
)
