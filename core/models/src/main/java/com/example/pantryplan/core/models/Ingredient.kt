package com.example.pantryplan.core.models

data class Ingredient(
    val name: String,
    val amount: Float,
    val measurement: Measurement,
    val linkedPantryItem: PantryItem?
)

enum class Measurement {
    GRAMS,
    KILOGRAMS,
    OTHER
}