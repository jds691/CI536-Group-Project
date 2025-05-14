package com.example.pantryplan.core.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class Ingredient(
    val name: String,
    val amount: Float,
    val measurement: Measurement,
    @Transient
    val linkedPantryItem: PantryItem? = null
)