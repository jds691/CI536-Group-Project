package com.example.pantryplan.core.data.access.repository

import android.util.Log
import com.example.pantryplan.core.database.dao.NutritionDao
import com.example.pantryplan.core.database.model.NutritionEntity
import com.example.pantryplan.core.models.NutritionInfo
import com.example.pantryplan.core.models.Recipe
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import javax.inject.Inject

fun NutritionInfo.asEntity(dateAndTime: Instant): NutritionEntity {
    val localDateTime = dateAndTime.toLocalDateTime(TimeZone.currentSystemDefault())

    return NutritionEntity(
        date = localDateTime.date,
        time = localDateTime.time,
        nutrition = this
    )
}

class NutritionRepositoryImpl @Inject constructor(
    private val nutritionDao: NutritionDao
) : NutritionRepository {
    override suspend fun logNutrients(meal: Recipe) {
        if (meal.nutrition.calories == 0) {
            Log.e("NutritionRepositoryImpl", "Calories for meal '${meal.id}' = 0, will not log!")
            return
        }

        nutritionDao.addNutrition(meal.nutrition.asEntity(Clock.System.now()))
    }

    override fun getLoggedMealCount(date: LocalDate): Flow<Int> {
        return nutritionDao.getDistinctLoggedMealCountForDateUntilChanged(date)
    }

    override fun getNutritionForDate(date: LocalDate): Flow<NutritionInfo> {
        return nutritionDao.getDistinctNutritionForDateUntilChanged(date).map { items ->
            var calories = 0
            var fats = 0f
            var saturatedFats = 0f
            var carbohydrates = 0f
            var sugar = 0f
            var fiber = 0f
            var protein = 0f
            var sodium = 0f

            for (item in items) {
                calories += item.nutrition.calories
                fats += item.nutrition.fats
                saturatedFats += item.nutrition.saturatedFats
                carbohydrates += item.nutrition.carbohydrates
                sugar += item.nutrition.sugar
                fiber += item.nutrition.fiber
                protein += item.nutrition.protein
                sodium += item.nutrition.sodium
            }

            NutritionInfo(
                calories,
                fats,
                saturatedFats,
                carbohydrates,
                sugar,
                fiber,
                protein,
                sodium
            )
        }
    }
}