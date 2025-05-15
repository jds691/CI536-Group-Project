package com.example.pantryplan.core.database.converter

import androidx.room.TypeConverter
import com.example.pantryplan.core.models.Ingredient
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json

internal class ListIngredientConverter {
    @TypeConverter
    fun ingredientListToString(value: List<Ingredient>?): String? {
        return try {
            Json.encodeToString(value)
        } catch (_: SerializationException) {
            null
        }
    }

    @TypeConverter
    fun stringToIngredientList(value: String?): List<Ingredient>? {
        return try {
            Json.decodeFromString<List<Ingredient>>(value ?: "")
        } catch (_: SerializationException) {
            null
        } catch (_: IllegalArgumentException) {
            null
        }
    }
    // This doesn't work for multiple entries, but just making it work first is more important.
}