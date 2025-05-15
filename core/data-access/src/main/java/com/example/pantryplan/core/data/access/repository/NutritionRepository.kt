package com.example.pantryplan.core.data.access.repository

import com.example.pantryplan.core.models.NutritionInfo
import com.example.pantryplan.core.models.Recipe
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

interface NutritionRepository {
    suspend fun logNutrients(meal: Recipe)
    fun getLoggedMealCount(date: LocalDate): Flow<Int>

    fun getNutritionForDate(date: LocalDate): Flow<NutritionInfo>
}