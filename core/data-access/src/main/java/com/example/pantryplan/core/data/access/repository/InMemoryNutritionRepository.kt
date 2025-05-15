package com.example.pantryplan.core.data.access.repository

import android.util.Log
import com.example.pantryplan.core.models.NutritionInfo
import com.example.pantryplan.core.models.Recipe
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.LocalDate
import javax.inject.Inject

private const val FLOW_REFRESH_DELAY: Long = 5000

class InMemoryNutritionRepository @Inject constructor() : NutritionRepository {
    private var nutrition: MutableList<NutritionInfo> = mutableListOf(
        NutritionInfo(
            calories = 600,
            fats = 20f,
            saturatedFats = 10f,
            carbohydrates = 40f,
            sugar = 8f,
            fiber = 20f,
            protein = 50f,
            sodium = 15f
        )
    )

    override suspend fun logNutrients(meal: Recipe) {
        val mealNutrition = meal.nutrition

        if (mealNutrition.calories == 0) {
            Log.e(
                "InMemoryNutritionRepository",
                "Calories for recipe '${meal.id}' = 0, ignoring from log."
            )
            return
        }

        nutrition.add(mealNutrition)
    }

    override fun getLoggedMealCount(): Flow<Int> = flow {
        while (true) {
            emit(nutrition.size)

            delay(FLOW_REFRESH_DELAY)
        }
    }

    override fun getNutritionForDate(date: LocalDate): Flow<NutritionInfo> = flow {
        // TODO: The in memory version can't keep track of nutrients across dates, it assumes everything logged is for today
        while (true) {
            var calories = 0
            var fats = 0f
            var saturatedFats = 0f
            var carbohydrates = 0f
            var sugar = 0f
            var fiber = 0f
            var protein = 0f
            var sodium = 0f

            for (loggedNutrition in nutrition) {
                calories += loggedNutrition.calories
                fats += loggedNutrition.fats
                saturatedFats += loggedNutrition.saturatedFats
                carbohydrates += loggedNutrition.carbohydrates
                sugar += loggedNutrition.sugar
                fiber += loggedNutrition.fiber
                protein += loggedNutrition.protein
                sodium += loggedNutrition.sodium
            }

            emit(
                NutritionInfo(
                    calories = calories,
                    fats = fats,
                    saturatedFats = saturatedFats,
                    carbohydrates = carbohydrates,
                    sugar = sugar,
                    fiber = fiber,
                    protein = protein,
                    sodium = sodium
                )
            )

            delay(FLOW_REFRESH_DELAY)
        }
    }
}