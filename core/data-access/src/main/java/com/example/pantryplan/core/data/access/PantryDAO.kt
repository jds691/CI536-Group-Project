package com.example.pantryplan.core.data.access

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import java.util.Date

@Dao
public interface PantryDAO {
    @Query("SELECT * FROM PantryStock")
    fun showAll(): List<PantryStock>

    @Query("SELECT * FROM PantryStock WHERE itemName LIKE :searchQuery")
    fun searchByName(searchQuery: String): PantryStock

    @Insert
    fun addItem(
        itemName: String,
        dateExpiring: Date,
        dateOpened: Date,
        quantity: Int,
        itemState: Int
        //TODO: Image reference URI
    )
}