package com.example.pantryplan.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.pantryplan.core.database.dao.PantryDao
import com.example.pantryplan.core.database.model.PantryStock

// Create Database with supported entities
@Database(
    entities = [
        PantryStock::class
    ],
    version = 1
)
internal abstract class PantryPlanDatabase : RoomDatabase() { //Database class
    abstract fun pantryDao(): PantryDao // Hook data entity up to interface
}