package com.example.pantryplan.core.data.access.repository

import com.example.pantryplan.core.models.PantryItem
import com.example.pantryplan.core.models.PantryItemState
import kotlinx.coroutines.flow.Flow
import java.util.UUID

/**
 * Data access repository for managing [PantryItem] objects.
 */
interface PantryItemRepository {
    /**
     * Adds a new [PantryItem] to the repository.
     *
     * @param[item] Item to add.
     */
    suspend fun addItemToRepository(item: PantryItem)

    /**
     * Removes an existing [PantryItem] from the repository.
     *
     * @param[item] Item to remove.
     */
    suspend fun removeItemFromRepository(item: PantryItem)

    /**
     * Gets all items currently stored into the repository.
     *
     * @return Stream of lists that contains every [PantryItem] in the repository at that time.
     */
    fun getAllItems(): Flow<List<PantryItem>>

    /**
     * Gets a [PantryItem] given its [id]
     *
     * @param[id] ID of the item to look up.
     *
     * @return Stream of items that correspond to the [id].
     */
    fun getItemById(id: UUID): Flow<PantryItem?>

    /**
     * Gets a [PantryItem] given its [barcode]
     *
     * @param[barcode] Barcode relating to the item to look up.
     *
     * @return Stream of items that correspond to the [barcode].
     */
    fun getItemByBarcode(barcode: String): Flow<PantryItem?>

    /**
     * Updates a [PantryItem] in the repository backend.
     *
     * @param[item] Item to update.
     */
    suspend fun updateItem(item: PantryItem)

    /**
     * Updates the state of a [PantryItem] given its [id].
     *
     * @param[id] ID of the item to update.
     * @param[state] New state to set item to.
     */
    suspend fun updateItemStateById(id: UUID, state: PantryItemState)

    /**
     * Performs a search in the repository attempting to match [PantryItem.name] to [name].
     *
     * @param[name] Name to use as the search term.
     *
     * @return Stream of lists containing all matching items
     */
    fun searchForItemsByName(name: String): Flow<List<PantryItem>>
}