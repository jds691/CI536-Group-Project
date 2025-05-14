package com.example.pantryplan.core.database.converter

import androidx.room.TypeConverter
import com.example.pantryplan.core.models.Allergen
import java.util.EnumSet

internal class AllergenConverter {
    @TypeConverter
    fun enumSetToStringList(value: EnumSet<Allergen>): List<Allergen>? {
        return value.toList()
    }
    @TypeConverter
    fun stringListToEnumSet(value: List<Allergen>): EnumSet<Allergen>? {
        return setOf(enumValueOf<Allergen>(value.toString())) as EnumSet<Allergen>?
    }
    // This doesn't work for multiple entries, but just making it work first is more important.
}