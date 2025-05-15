package com.example.pantryplan.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.pantryplan.core.database.model.RecipeInformation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import java.util.UUID

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
     * Retrieve recipe with specified UUID or name
     */
    @Query("SELECT * FROM RecipeInformation WHERE :uuidSearched = recipeUUID")
    fun searchById(uuidSearched: UUID): Flow<RecipeInformation?>
    fun searchDistinctByIdUntilChanged(id: UUID): Flow<RecipeInformation?> =
        searchById(id).distinctUntilChanged()

    @Query("SELECT * FROM RecipeInformation WHERE :nameSearched LIKE recipeName")
    fun searchByName(nameSearched: String): Flow<RecipeInformation>

    /**
     * Retrieves all items of a given tag
     *
     * @return stream containing [RecipeInformation] of the same [tag]
     */
    @Query("SELECT * FROM RecipeInformation WHERE :tag LIKE '%' || recipeTags || '%'")
    fun filterByTag(tag: String): Flow<RecipeInformation>

    /**
     * Update recipe in database
     */
    @Update
    suspend fun updateRecipe(recipeInformation: RecipeInformation)

    @Delete
    suspend fun removeRecipe(recipeInformation: RecipeInformation)

    /**
     * Create new recipe
     */
    @Insert
    suspend fun addRecipe(recipeInformation: RecipeInformation)
}