package com.example.pantryplan.core.database.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.pantryplan.core.models.Allergen
import com.example.pantryplan.core.models.NutritionInfo
import com.example.pantryplan.core.models.Recipe
import java.util.EnumSet
import java.util.UUID

@Entity(tableName = "RecipeInformation")
data class RecipeInformation(
    @PrimaryKey
    val recipeUUID: UUID,
    val recipeName: String,
    val recipeAllergens: EnumSet<Allergen>,
    val recipePrepTime: Float,
    val recipeCookTime: Float,
    val recipeDesc: String,
    val recipeIngredients: List<String>,
    @Embedded val nutritionalInformation: NutritionInfo,
    val recipeTags: List<String>,
    val imageUrl: String,
    val recipeInstructions: List<String>
)

fun RecipeInformation.asExternalModel() = Recipe(
    id = recipeUUID,
    title = recipeName,
    allergens = recipeAllergens,
    description = recipeDesc,
    tags = recipeTags,
    ingredients = recipeIngredients,
    prepTime = recipePrepTime,
    cookTime = recipeCookTime,
    nutrition = nutritionalInformation,
    imageUrl = imageUrl,
    instructions = recipeInstructions
)