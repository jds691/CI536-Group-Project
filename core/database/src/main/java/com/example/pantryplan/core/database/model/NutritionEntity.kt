package com.example.pantryplan.core.database.model

import androidx.room.Embedded
import androidx.room.Entity
import com.example.pantryplan.core.models.NutritionInfo
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

@Entity(
    tableName = "nutrition",
    primaryKeys = ["date", "time"]
)
data class NutritionEntity(
    val date: LocalDate,
    val time: LocalTime,
    @Embedded
    val nutrition: NutritionInfo
)