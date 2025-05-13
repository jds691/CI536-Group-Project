package com.example.pantryplan.core.data.access.repository

import com.example.pantryplan.core.models.Recipe
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.util.UUID

class InMemoryRecipeRepository : RecipeRepository {
    private var recipes: MutableList<Recipe> = mutableListOf()
    private val FLOW_REFRESH_DELAY: Long = 5000

    override fun getAllRecipes(): Flow<List<Recipe>> = flow {
        while (true) {
            emit(recipes)
            delay(FLOW_REFRESH_DELAY)
        }
    }

    override fun getRecipeById(id: UUID): Flow<Recipe?> = flow {
        while (true) {
            try {
                val existingRecipe = recipes.first { it.id == id }
                emit(existingRecipe)
            } catch (e: NoSuchElementException) {
                emit(null)
            }

            delay(FLOW_REFRESH_DELAY)
        }
    }

    override suspend fun addRecipe(recipe: Recipe) {
        recipes.add(recipe)
    }

    override suspend fun addRecipeFromJson(json: String) {
        try {
            val recipe = Json.decodeFromString<Recipe>(json)
            addRecipe(recipe)
        } catch (_: SerializationException) {

        } catch (_: IllegalArgumentException) {

        }
    }

    override suspend fun removeItem(recipe: Recipe) {
        recipes.remove(recipe)
    }

    override suspend fun updateRecipe(recipe: Recipe) {
        try {
            val existingRecipe = recipes.first { it.id == recipe.id }
            val index = recipes.indexOf(existingRecipe)
            recipes[index] = recipe
        } catch (_: NoSuchElementException) {

        }
    }

    override suspend fun getJsonForRecipe(recipe: Recipe): String? {
        return try {
            Json.encodeToString(recipe)
        } catch (_: SerializationException) {
            null
        }
    }
}