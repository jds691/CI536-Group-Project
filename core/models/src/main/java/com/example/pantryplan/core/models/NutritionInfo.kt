package com.example.pantryplan.core.models

import kotlinx.serialization.Serializable

@Serializable
data class NutritionInfo(
    val calories: Int,
    val fats: Float,
    val saturatedFats: Float,
    val carbohydrates: Float,
    val sugar: Float,
    val fiber: Float,
    val protein: Float,
    val sodium: Float
)
