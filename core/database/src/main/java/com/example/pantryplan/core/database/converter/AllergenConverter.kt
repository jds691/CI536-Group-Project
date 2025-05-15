package com.example.pantryplan.core.database.converter

import androidx.room.TypeConverter
import com.example.pantryplan.core.models.Allergen
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.util.EnumSet

internal class AllergenConverter {
    @TypeConverter
    fun enumSetToString(value: EnumSet<Allergen>?): String? {
        val allergenList = value?.toMutableList()

        return try {
            Json.encodeToString(allergenList)
        } catch (_: SerializationException) {
            null
        }
    }
    @TypeConverter
    fun stringListToEnumSet(value: String?): EnumSet<Allergen>? {
        try {
            val list = Json.decodeFromString<List<Allergen>>(value ?: "")
            return if (list.isEmpty())
                EnumSet.noneOf(Allergen::class.java)
            else
                EnumSet.copyOf(list)
        } catch (_: SerializationException) {
            return null
        } catch (_: IllegalArgumentException) {
            return null
        }
    }
    // This doesn't work for multiple entries, but just making it work first is more important.
}