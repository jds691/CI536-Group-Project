package com.example.pantryplan.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.pantryplan.core.database.model.PantryStock
import com.example.pantryplan.core.models.PantryItemState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import java.util.UUID

/**
 * Data access object for [PantryStock] instances in [com.example.pantryplan.core.database.PantryPlanDatabase].
 */
@Dao
interface PantryDao {
    /**
     * Retrieves all items from the database.
     *
     * @return Stream of stock items
     */
    @Query("SELECT * FROM PantryStock")
    fun showAll(): Flow<List<PantryStock>>

    /**
     * Attempts to retrieve an item from the database given it's [id].
     *
     * Flow will emit if **any** row in the database changes. Consider using [searchDistinctByIdUntilChanged] instead unless you know what you are doing.
     *
     * @param[id] ID of the stock item to lookup.
     *
     * @return Stream containing the [PantryStock] corresponding to the [id] or null
     */
    @Query("SELECT * FROM PantryStock WHERE itemID = :id")
    fun searchById(id: UUID): Flow<PantryStock?>

    /**
     * Attempts to retrieve an item from the database given it's [id].
     *
     * Flow will only emit if the item relating to this id is changed.
     *
     * @param[id] ID of the stock item to lookup.
     *
     * @return Stream containing the [PantryStock] corresponding to the [id] or null
     */
    fun searchDistinctByIdUntilChanged(id: UUID): Flow<PantryStock?> =
        searchById(id).distinctUntilChanged()

    /**
     * Attempts to retrieve an item from the database given it's [barcode].
     *
     * Flow will emit if **any** row in the database changes. Consider using [getDistinctStockByBarcodeUntilChanged] instead unless you know what you are doing.
     *
     * @param[barcode] Barcode of the stock item to lookup.
     *
     * @return Stream containing the [PantryStock] corresponding to the [barcode] or null
     */
    @Query("SELECT * FROM PantryStock WHERE barcode = :barcode")
    fun getStockByBarcode(barcode: String): Flow<PantryStock?>

    /**
     * Attempts to retrieve an item from the database given it's [barcode].
     *
     * Flow will only emit if the item relating to this barcode is changed.
     *
     * @param[barcode] Barcode of the stock item to lookup.
     *
     * @return Stream containing the [PantryStock] corresponding to the [barcode] or null
     */
    fun getDistinctStockByBarcodeUntilChanged(barcode: String): Flow<PantryStock?> =
        getStockByBarcode(barcode).distinctUntilChanged()

    /**
     * Performs a generic search over all [PantryStock] item names.
     *
     * Wilds cards are not automatically included and are recommended to be passed in.
     * @sample[fuzzySearchWithWildcards]
     *
     * @param[name] Search term to compare to [PantryStock.itemName].
     *
     * @return Stream containing a list of all [PantryStock] where [PantryStock.itemName] contains [name].
     */
    @Query("SELECT * FROM PantryStock WHERE itemName LIKE :name")
    fun fuzzySearchByName(name: String): Flow<List<PantryStock>>

    /**
     * Gets all [PantryStock] items in a particular state.
     *
     * @param[state] State to use for lookup.
     *
     * @return Stream containing a list of [PantryStock] where [PantryStock.itemState] = [state]
     */
    @Query("Select * FROM PantryStock WHERE itemState = :state")
    fun getItemsByState(state: PantryItemState): Flow<List<PantryStock>>

    // Used by a function that wasn't required by the DB
    // check date opened and compare to date
    // expiring, then return time until expiry

    /**
     * Inserts a new [PantryStock] into the database.
     *
     * @param[item] Item to insert.
     */
    @Insert
    suspend fun addItem(/*
WWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWNNO0kKWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWW
WWWWWWWWWWWWWWWWWWWWWWWWWWWWWXOl;,,;;cc,.   .':cl0XWWNWNWWWWWWWWWWWWWWWWWWWWWWWWWWWWW
WWWWWWWWWWWWWWWWWWWWWWWWWNWOcc...                 ..,,'o0XNWWNWWWWWWWWWWWWWWWWWWWWWWW
WWWWWWWWWWWWWWWWWWWWWWN0do;..                            ....kNWWWWWWWWWWWWWWWWWWWWWW
WWWWWWWWWWWWWWWWWWWWKc.                                       :NWWWWWWWWWWWWWWWWWWWWW
WWWWWWWWWWWWWWWWWWWd.                                          cXWWWWWWWWWWWWWWWWWWWW
WWWWWWWWWWWWWWWWWK:.                                            .cdKWWWWWWWWWWWWWWWWW
WWWWWWWWWWWWWWWWK'                                                .,lkXNWWWWWWWWWWWWW
WWWWWWWWWWWWWWWN;.    ...                                            .'0WWWWWWWWWWWWW
WWWWWWWWWWWWWWWX.    .                                                .oXWWWWWWWWWWWW
WWWWWWWWWWWWWWWd    .       .                                          .xNWWWWWWWWWWW
WWWWWWWWWWWWWWW,   .    .'...'.........                                 kWWWWWWWWWWWW
WWWWWWWWWWWWWW0..      .;;:.';::::;;,...                                 XWWWWWWWWWWW
WWWWWWWWWWWWWN'  . .   .,lll;::cclc:::;,'.....                           xWWWWWWWWWWW
WWWWWWWWWWWNX'     .....';oddlcc:cllooolc;'....  ..                      cWWWWWWWWWWW
WWWWWWWWWWWWd         ..;:coxkkxdlcoddxddocc,.   ....                     OWWWWWWWWWW
WWWWWWWWWWWWd        ..,ldxxkOOOkkxxxxxxxxxolc'.  .....                   .dWWWWWWWWW
WWWWWWWWWWWW0       ..,lxkkOOOOOOOOOOOOOkkkxdol;.  .....                  .xWWWWWWWWW
WWWWWWWWWWWWW,     ..:oxxkkOOOOOOOOOOOOOOOkkxxdolc;'........               lONWWWWWWW
WWWWWWWWWWWWWc      :dxxkkOOOOOOOOOOOOOOOOkxxxxxxdolc:,'''...              .xNWWWWWWW
WWWWWWWWWWWWXo     .lxxxxxxxddodxxkkOkkOkkxdddoloolcccc;,,;,..            .ONWWWWWWWW
WWWWWWWWWWWWO.     .dxxdxdol;''';:cokkOko::;,'',;:ccc:;;;;,,'.             cNNNWWWWWW
WWWWWWWWWWWWK.     ;xxkxdllo,:';lcddkOOxc,,:::c:;.',;:c:c:;,,,'.           ;lcNWWWWWW
WWWWWWWWWWWWWk. ...lxkOkkkkkkddddxxxOOOxc;;lodkxlcccc;ccllc:;;'.          ..;KWNWWWWW
WWWWWWWWWWWWWWl.l,'lkkOOOOOOOOkkkkkkOOkdc;:cddxxxxdoloddoolc;;'.    .... .,.;ONWWWWWW
WWWWWWWWWWWWWWl.:l;:xkOOOOO0O00OOOkkOOxo:;:codxkkkxxxddxddo:;,'.   ....,. ..xNNWWWWWW
WWWWWWWWWWWWWWX,.kx,xkO0O00O0000OkkOOOxl:::coxkkkkkxxxxxdol:,,'.   .,,',..;ONWWWWWWWW
WWWWWWWWWWWWWWWN:OOldkOOO000000OOkkOOOxoc;;:oxkOOOkxxxxdolc;,''. ...;:,;:0KWWWWWWWWWW
WWWWWWWWWWWWWWWWOOOxxkkOO000000OOkOOOOxoc;::lxOO0OOkkxdoll:;,'......,:,;dXWWWWWWWWWWW
WWWWWWWWWWWWWWWWWOOOkxkOO000OOO0OkkO0Odllclc;okOOOkkkxdolc:;,'..'..';;;OWWWWWWWWWWWWW
WWWWWWWWWWWWWWWWWXOOkxkOOOO0OOOOOOxkxdl:;;:::oxkkkkkxdollc:,,'..,;;,;;xWWWWWWWWWWWWWW
WWWWWWWWWWWWWWWWWWX0kxkkOOOOOOOOOkkOkxxdolcldxddxkkxxolllc:,,'.';;:::OWWWWWWWWWWWWWWW
WWWWWWWWWWWWWWWWWWWWXxxkkOOOkkkkkOOOOkOOxdolloooodkxdocclc:,,'. .,.;WWWWWWWWWWWWWWWWW
WWWWWWWWWWWWWWWWWWWWWOxkkkOOkkxxdddoodddoolc:::lxxxxol:cl:;;;'.  .dWWWWWWWWWWWWWWWWWW
WWWWWWWWWWWWWWWWWWWWWNOxkkkOOkOOkkkxkkkxdolllloxxxxdl::ll:;:,,odOXWWWWWWWWWWWWWWWWWWW
WWWWWWWWWWWWWWWWWWWWWWNkkkkkkkkOOOkkkkkxxdoooododddl::llc::;;XWWWWWWWWWWWWWWWWWWWWWWW
WWWWWWWWWWWWWWWWWWWWWWWNOkkkkOOkkOOkxxxxxxxxdoooodlccllc;,,oNWWWWWWWWWWWWWWWWWWWWWWWW
WWWWWWWWWWWWWWWWWWWWWWWWWKkkkkOkOOOOOOkkkxxxddolol:clc:,,dNWWWWWWWWWWWWWWWWWWWWWWWWWW
WWWWWWWWWWWWWWWWWWWWWWWWWWXOkkOOOOO0OOOOkkxdddoolc::;;ckNWWWWWWWWWWWWWWWWWWWWWWWWWWWW
WWWWWWWWWWWWWWWWWWWWWWWWWWWWNOkO0000OkkOkkxxdolc;;;lONWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWW
WWWWWWWWWWWWWWWWWWWWWWWWWWWWWWX0kOOkkkkkkxddol:;lkKWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWW
WWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWNX0OkkkkkkkkkO0NWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWW
WWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWW
It just works.*/
                        item: PantryStock
    )

    /**
     * Updates an item in the database.
     *
     * @param[item] Item to update.
     */
    @Update
    suspend fun updateItem(item: PantryStock)

    /**
     * Updates the state of a [PantryStock].
     *
     * @param[stateUpdate] [PantryStockStateUpdate] data class to use.
     *
     * @sample[updateItemStateUsage]
     */
    @Update(entity = PantryStock::class)
    suspend fun updateItemState(stateUpdate: PantryStockStateUpdate)

    /**
     * Removes a [PantryStock] instance from the database.
     *
     * @param[item] Item to remove.
     */
    @Delete
    suspend fun removeItem(item: PantryStock)

    /**
     * Removes a [PantryStock] from the database by its [id].
     *
     * @param[id] ID of the item to remove.
     */
    @Query("DELETE FROM PantryStock WHERE itemID = :id")
    suspend fun removeItemById(id: UUID) //Delete row containing specified item UUID
}

/**
 * Helper data class to perform state updates by ID
 *
 * @property[itemID] ID of the item to change
 * @property[itemState] New state to set item to
 */
data class PantryStockStateUpdate(
    val itemID: UUID,
    val itemState: PantryItemState,
)

//MARK: Documentation examples - DO NOT CALL
private fun fuzzySearchWithWildcards(dao: PantryDao, searchTerm: String): Flow<List<PantryStock>> {
    return dao.fuzzySearchByName("%$searchTerm%")
}

private suspend fun updateItemStateUsage(dao: PantryDao, item: PantryStock) {
    dao.updateItemState(
        PantryStockStateUpdate(
            itemID = item.itemID,
            itemState = PantryItemState.FROZEN
        )
    )
}