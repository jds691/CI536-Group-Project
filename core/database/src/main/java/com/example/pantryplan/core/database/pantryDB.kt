package com.example.pantryplan.core.database

import android.media.Image
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.Update
import java.sql.Date
import java.util.UUID

@Entity(tableName = "PantryStock")
data class PantryStock(
    @PrimaryKey val itemID: UUID,
    val itemName: String,
    val dateExpiring: Date,
    val dateOpened: Date?, // can be null
    val quantity: Int,
    val itemState: Int, // [0..3] - 0 sealed, 1 opened, 2 frozen, 3 expired
    val imageRefURL: String? // Can be null, TODO: Path
)


// Create Database with above class as fields
@Database(entities = [PantryStock::class], version = 1)
    abstract class PantryDB : RoomDatabase() { //Database class
        abstract fun pantryDAO(): PantryDAO.PantryDAO // Hook data entity up to interface
    }

/*
    For days until expiration, create a method to
    check date opened and compare to date expiring,
    then return time until expiry
*/

@Dao
abstract class PantryDAO {
    interface PantryDAO {
        @Query("SELECT * FROM PantryStock")
        suspend fun showAll(): List<String>

        @Query("SELECT * FROM PantryStock WHERE itemName LIKE :searchQuery")
        suspend fun searchByName(searchQuery: String): PantryStock

        @Query("Select * FROM PantryStock WHERE itemState LIKE :searchQuery")
        suspend fun readFromState(searchQuery: String)

        @Query("SELECT itemID, itemName, dateOpened, dateExpiring, itemState FROM pantrystock")
        suspend fun calcDaysUntilExpired(): List<String>
        //TODO; check date opened and compare to date,
        // expiring then return time until expiry

        @Insert
        suspend fun addItem(
            itemName: String,
            dateExpiring: java.util.Date,
            dateOpened: java.util.Date,
            quantity: Int,
            itemState: Int
            //TODO: Image reference URI
        )

        @Update (entity = PantryDB::class)
        suspend fun updateItemState(itemState: Int)

        @Update (entity = UUID::class)
        suspend fun updateItem(itemName: String, dateExpiring: Date, dateOpened: Date,
                               quantity: Int, itemState: Int, imageRefURL: String)
        //TODO: Come back to update functions after nap


        @Delete
        suspend fun removeItem(itemID: UUID); //Delete row containing specified item UUID
    }
}