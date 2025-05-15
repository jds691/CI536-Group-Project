package com.example.pantryplan.core.database.converter

import androidx.room.TypeConverter
import kotlinx.datetime.LocalDate

internal class LocalDateConverter {
    @TypeConverter
    fun intToLocalDate(value: Int?): LocalDate? =
        value?.let(LocalDate::fromEpochDays)

    @TypeConverter
    fun localDateToInt(localDate: LocalDate?): Int? =
        localDate?.toEpochDays()
}