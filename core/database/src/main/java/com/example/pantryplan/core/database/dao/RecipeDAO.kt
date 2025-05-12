package com.example.pantryplan.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.pantryplan.core.database.model.RecipeInformation
import com.example.pantryplan.core.models.Recipe
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {
    /**
     * Retrieves all recipes from database.
     *
     * @return stream of recipes
     */
    @Query("SELECT * FROM RecipeInformation")
    fun showAllRecipes(): Flow<List<RecipeInformation>>

    /**
     * Retrieves all items of a given tag
     *
     * @return stream containing [RecipeInformation] of the same [tag]
     */
    @Query("SELECT * FROM RecipeInformation WHERE recipeTags = :tag")
    fun filterByTag(tag: List<String>): Flow<RecipeInformation>

    /**
     * Deletes all items from a user selection
     */
    @Delete
    suspend fun removeRecipe(recipe: List<Recipe>)

    /**
     * Create new recipe
     */
    @Insert
    suspend fun addRecipe(recipe: Recipe)

}