package com.example.pantryplan.core.data.access.repository

import android.util.Log
import com.example.pantryplan.core.database.dao.RecipeDao
import com.example.pantryplan.core.database.model.RecipeInformation
import com.example.pantryplan.core.database.model.asExternalModel
import com.example.pantryplan.core.models.Recipe
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.util.UUID
import javax.inject.Inject

fun Recipe.asEntity() = RecipeInformation(
    recipeUUID = id,
    recipeName = title,
    recipeDesc = description,
    recipeAllergens = allergens,
    recipePrepTime = prepTime,
    recipeCookTime = cookTime,
    recipeIngredients = ingredients,
    nutritionalInformation = nutrition,
    recipeTags = tags,
    imageUrl = imageUrl,
    recipeInstructions = instructions,
)

class RecipeRepositoryImpl @Inject constructor(
    private val recipeDao: RecipeDao
) : RecipeRepository {
    override fun getAllRecipes(): Flow<List<Recipe>> {
        return recipeDao.showAllRecipes().map { it.map(RecipeInformation::asExternalModel) }
    }

    override fun getRecipeById(id: UUID): Flow<Recipe?> {
        return recipeDao.searchDistinctByIdUntilChanged(id).map { it?.asExternalModel() }
    }

    override suspend fun addRecipe(recipe: Recipe) {
        recipeDao.addRecipe(recipe.asEntity())
    }

    override suspend fun addRecipeFromJson(json: String) {
        try {
            recipeDao.addRecipe(Json.decodeFromString<Recipe>(json).asEntity())
        } catch (_: SerializationException) {
            Log.e("RecipeRepositoryImpl", "Unable to deserialize recipe")
        } catch (_: IllegalArgumentException) {
            Log.e("RecipeRepositoryImpl", "Unable to deserialize recipe")
        }
    }

    override suspend fun removeItem(recipe: Recipe) {
        recipeDao.removeRecipe(recipe.asEntity())
    }

    override suspend fun updateRecipe(recipe: Recipe) {
        recipeDao.updateRecipe(recipe.asEntity())
    }

    override suspend fun getJsonForRecipe(recipe: Recipe): String? {
        try {
            return Json.encodeToString(recipe)
        } catch (_: SerializationException) {
            Log.e("RecipeRepositoryImpl", "Unable to serialize recipe '${recipe.id}' to JSON")
            return null
        }
    }

    override suspend fun getRandomRecipe(): Recipe {
        return getAllRecipes().first().random()
    }
}