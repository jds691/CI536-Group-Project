package com.example.pantryplan.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.pantryplan.core.database.model.PantryStock
import com.example.pantryplan.core.models.PantryItemState
import java.util.UUID

@Dao
interface PantryDao {
    @Query("SELECT * FROM PantryStock")
    suspend fun showAll(): List<PantryStock>

    @Query("SELECT * FROM PantryStock WHERE itemID = :id")
    suspend fun searchById(id: UUID): PantryStock

    @Query("SELECT * FROM PantryStock WHERE itemName LIKE :name")
    suspend fun fuzzySearchByName(name: String): PantryStock

    @Query("Select * FROM PantryStock WHERE itemState = :state")
    suspend fun getItemsByState(state: PantryItemState): List<PantryStock>

    // Used by a function that wasn't required by the DB
    // check date opened and compare to date
    // expiring, then return time until expiry

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

    @Update
    suspend fun updateItem(item: PantryStock)

    @Update(entity = PantryStock::class)
    suspend fun updateItemState(stateUpdate: PantryStockStateUpdate)

    @Delete
    suspend fun removeItem(item: PantryStock)

    @Query("DELETE FROM PantryStock WHERE itemID = :id")
    suspend fun removeItemById(id: UUID) //Delete row containing specified item UUID
}

data class PantryStockStateUpdate(
    val itemID: UUID,

    val itemState: PantryItemState,
)