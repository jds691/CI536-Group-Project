package com.example.pantryplan.feature.meals

import androidx.compose.runtime.Composable
import com.example.pantryplan.core.models.NutritionInfo
import com.example.pantryplan.feature.meals.ui.MacrosCard


@Composable
fun MealPlannerScreen() {
    //example card
    MacrosCard(
        item = NutritionInfo(
            calories = 500,
            fats = 13.35f,
            saturatedFats = 22f,
            carbohydrates = 65f,
            sugar = 12f,
            fiber = 34f,
            protein= 30f,
            sodium = 12f
        )
    )
}
