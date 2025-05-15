package com.example.pantryplan.core.data.access.repository

import com.example.pantryplan.core.models.Recipe
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface RecipeRepository {
    fun getAllRecipes(): Flow<List<Recipe>>
    fun getRecipeById(id: UUID): Flow<Recipe?>

    suspend fun addRecipe(recipe: Recipe)
    suspend fun addRecipeFromJson(json: String)
    suspend fun removeItem(recipe: Recipe)

    suspend fun updateRecipe(recipe: Recipe)

    suspend fun getJsonForRecipe(recipe: Recipe): String?

    suspend fun getRandomRecipe(): Recipe
}