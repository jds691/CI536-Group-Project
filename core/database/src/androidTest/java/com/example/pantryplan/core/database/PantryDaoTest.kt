package com.example.pantryplan.core.database

import com.example.pantryplan.core.database.model.PantryStock
import com.example.pantryplan.core.models.PantryItemState
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Clock
import org.junit.Test
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.time.Duration.Companion.days

internal class PantryDaoTest : DatabaseTest() {
    @Test
    fun getAllItems() = runTest {
        val firstItem = testPantryStock(
            name = "Test"
        )
        val secondItem = testPantryStock(
            name = "Test 2"
        )

        pantryDao.addItem(firstItem)
        pantryDao.addItem(secondItem)

        val items = pantryDao.showAll()

        assertEquals(items.size, 2)

        assertEquals(secondItem.itemID, items[1].itemID)
    }

    @Test
    fun getItemById() = runTest {
        val item = testPantryStock(
            name = "Test"
        )

        pantryDao.addItem(item)
        val searchedItem = pantryDao.searchById(item.itemID)

        // Checking the entire object fails due to floating point errors on the Instants
        assertEquals(item.itemName, searchedItem.itemName)
        assertEquals(item.itemState, searchedItem.itemState)
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

        val items = pantryDao.fuzzySearchByName("%Milk%")

        assertEquals(3, items.size)
        assertFalse(items.contains(borger))
    }
}

private fun testPantryStock(
    id: UUID = UUID.randomUUID(),
    name: String,
    state: PantryItemState = PantryItemState.OPENED
) = PantryStock(
    itemID = id,
    itemName = name,
    dateExpiring = Clock.System.now().plus(7.days),
    dateOpened = Clock.System.now(),
    quantity = 0,
    inStateSince = Clock.System.now(),
    itemState = state,
    imageRefURL = null
)
