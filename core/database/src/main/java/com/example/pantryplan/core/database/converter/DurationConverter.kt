package com.example.pantryplan.core.database.converter

import androidx.room.TypeConverter
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

internal class DurationConverter {
    @TypeConverter
    fun longToDuration(value: Long?): Duration? =
        value?.toDuration(DurationUnit.MILLISECONDS)

    @TypeConverter
    fun durationToLong(duration: Duration?): Long? =
        duration?.toLong(DurationUnit.MILLISECONDS)
}