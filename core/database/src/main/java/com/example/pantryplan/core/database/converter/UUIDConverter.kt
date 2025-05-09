package com.example.pantryplan.core.database.converter

import androidx.room.TypeConverter
import java.util.UUID

internal class UUIDConverter {
    @TypeConverter
    fun stringToUuid(value: String?): UUID? =
        value?.let(UUID::fromString)

    @TypeConverter
    fun uuidToString(uuid: UUID?): String? =
        uuid?.toString()
}
