package com.example.pantryplan.core.database

import androidx.room.Database
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.RoomDatabase
import java.sql.Date
import java.util.UUID
import com.example.pantryplan.core.data.access.PantryDAO // ???

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

@Database(entities = [PantryStock::class], version = 1)
    abstract class PantryDB : RoomDatabase() {
        abstract fun pantryDAO(): PantryDAO
    }

/*
    For days until expiration, create a method to
    check date opened and compare to date expiring,
    then return time until expiry
*/