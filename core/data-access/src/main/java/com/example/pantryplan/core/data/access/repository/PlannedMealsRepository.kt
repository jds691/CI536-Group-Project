package com.example.pantryplan.core.data.access.repository

import com.example.pantryplan.core.models.Recipe
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

interface PlannedMealsRepository {
    suspend fun getPlannedMealsForDate(date: LocalDate, limit: Int): Flow<List<Recipe>>
}