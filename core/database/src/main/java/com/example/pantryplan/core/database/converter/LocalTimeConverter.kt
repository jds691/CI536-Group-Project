package com.example.pantryplan.core.database.converter

import androidx.room.TypeConverter
import kotlinx.datetime.LocalTime

internal class LocalTimeConverter {
    @TypeConverter
    fun intToLocalTime(value: Int?): LocalTime? =
        value?.let(LocalTime::fromMillisecondOfDay)

    @TypeConverter
    fun localTimeToInt(localTime: LocalTime?): Int? =
        localTime?.toMillisecondOfDay()
}