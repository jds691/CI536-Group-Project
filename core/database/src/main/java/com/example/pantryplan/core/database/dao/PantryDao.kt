package com.example.pantryplan.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.pantryplan.core.database.PantryPlanDatabase
import com.example.pantryplan.core.database.model.PantryStock
import kotlinx.datetime.Instant
import java.util.UUID

@Dao
interface PantryDao {
    @Query("SELECT * FROM PantryStock")
    suspend fun showAll(): List<String>

    @Query("SELECT * FROM PantryStock WHERE itemName LIKE :searchQuery")
    suspend fun searchByUUID(searchQuery: String): PantryStock

    @Query("SELECT * FROM PantryStock WHERE itemName LIKE :searchQuery")
    suspend fun fuzzySearchByName(searchQuery: String): PantryStock

    @Query("Select * FROM PantryStock WHERE itemState LIKE :searchQuery")
    suspend fun readFromState(searchQuery: String)

    @Query("SELECT itemID, itemName, dateOpened, dateExpiring, itemState FROM pantrystock")
    suspend fun calcDaysUntilExpired() {

    }
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
    ): PantryStock

    @Update(entity = PantryPlanDatabase::class)
    suspend fun updateItemState(itemState: Int)

    @Update(entity = UUID::class)
    suspend fun updateItem(
        itemName: String, dateExpiring: Instant, dateOpened: Instant,
        quantity: Int, itemState: Int, imageRefURL: String
    )
    //This *probably* works. Target specification is unclear, though

    @Delete
    suspend fun removeItem(itemID: UUID) //Delete row containing specified item UUID
}