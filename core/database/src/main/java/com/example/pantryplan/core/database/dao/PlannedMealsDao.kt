package com.example.pantryplan.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.pantryplan.core.database.model.PlannedMeal
import com.example.pantryplan.core.database.model.PlannedMealEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

@Dao
interface PlannedMealsDao {
    @Query(
        "SELECT RecipeInformation.*, PlannedMeal.* FROM RecipeInformation INNER JOIN PlannedMeal ON RecipeInformation.recipeUUID = PlannedMeal.recipeId WHERE PlannedMeal.date = :date LIMIT :limit"
    )
    fun getMealsForDate(date: LocalDate, limit: Int): Flow<List<PlannedMealEntity>>

    @Query("SELECT COUNT(*) FROM PlannedMeal WHERE date = :date")
    fun getPlannedMealsCountForDate(date: LocalDate): Flow<Int>

    @Insert
    suspend fun addPlannedMealForDate(plannedMeal: PlannedMeal)
}