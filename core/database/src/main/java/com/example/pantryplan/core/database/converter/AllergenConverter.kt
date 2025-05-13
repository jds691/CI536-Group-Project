package com.example.pantryplan.core.database.converter

import androidx.room.TypeConverter
import com.example.pantryplan.core.models.Allergen
import java.util.EnumSet

internal class AllergenConverter {
    @TypeConverter
    fun enumSetToList(value: EnumSet<Allergen>?) = value?.toList()

    @TypeConverter
    fun listToEnumSet(value: List<String>?) = enumValueOf<Allergen>(value.toString())
}