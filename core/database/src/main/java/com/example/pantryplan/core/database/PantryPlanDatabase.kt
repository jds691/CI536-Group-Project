package com.example.pantryplan.core.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.pantryplan.core.database.converter.AllergenConverter
import com.example.pantryplan.core.database.converter.DurationConverter
import com.example.pantryplan.core.database.converter.InstantConverter
import com.example.pantryplan.core.database.converter.ListIngredientConverter
import com.example.pantryplan.core.database.converter.ListStringConverter
import com.example.pantryplan.core.database.converter.UUIDConverter
import com.example.pantryplan.core.database.dao.PantryDao
import com.example.pantryplan.core.database.dao.RecipeDao
import com.example.pantryplan.core.database.model.PantryStock
import com.example.pantryplan.core.database.model.RecipeInformation

// Create Database with supported entities
@Database(
    version = 2,
    autoMigrations = [
        AutoMigration(from = 1, to = 2)
    ],
    entities = [
        PantryStock::class,
        RecipeInformation::class
    ]
)
@TypeConverters(
    InstantConverter::class,
    DurationConverter::class,
    UUIDConverter::class,
    AllergenConverter::class,
    ListStringConverter::class,
    ListIngredientConverter::class
)
internal abstract class PantryPlanDatabase : RoomDatabase() { //Database class
    abstract fun pantryDao(): PantryDao // Hook data entities up to interface
    abstract fun recipeDao(): RecipeDao
}