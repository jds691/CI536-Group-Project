package com.example.pantryplan.core.models

import java.util.EnumSet
import java.util.UUID

data class Recipe(
    val id: UUID,
    val title: String,
    val description: String,
    val tags: List<String>,
    val allergens: EnumSet<Allergen>,
    val imageUrl: String?,
    val instructions: List<String>,
    // REVIEW: Is this suitable enough for generic referencing of pantry items
    val ingredients: List<Ingredient>,

    // Metadata
    val prepTime: Float,
    val cookTime: Float,
    val nutrition: NutritionInfo
)
