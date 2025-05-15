package com.example.pantryplan.core.database.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDate
import java.util.UUID

@Entity
data class PlannedMeal(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val recipeId: UUID,
    val date: LocalDate
)

data class PlannedMealEntity(
    @Embedded
    val recipe: RecipeInformation,
    @Embedded
    val plannedMeal: PlannedMeal
)