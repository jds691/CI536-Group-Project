package com.example.pantryplan.core.database.converter

import androidx.room.TypeConverter
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json

internal class ListStringConverter {
    @TypeConverter
    fun stringListToString(value: List<String>?): String? {
        return try {
            Json.encodeToString(value)
        } catch (_: SerializationException) {
            null
        }
    }

    @TypeConverter
    fun stringToStringList(value: String?): List<String>? {
        return try {
            Json.decodeFromString<List<String>>(value ?: "")
        } catch (_: SerializationException) {
            null
        } catch (_: IllegalArgumentException) {
            null
        }
    }
    // This doesn't work for multiple entries, but just making it work first is more important.
}