package com.example.pantryplan.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.pantryplan.core.database.model.NutritionEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.datetime.LocalDate

@Dao
interface NutritionDao {
    @Insert
    suspend fun addNutrition(nutrition: NutritionEntity)

    @Query("SELECT COUNT(*) FROM nutrition WHERE date = :date")
    fun getLoggedMealCountForDate(date: LocalDate): Flow<Int>
    fun getDistinctLoggedMealCountForDateUntilChanged(date: LocalDate): Flow<Int> =
        getLoggedMealCountForDate(date).distinctUntilChanged()

    @Query("SELECT * FROM nutrition WHERE date = :date")
    fun getNutritionForDate(date: LocalDate): Flow<List<NutritionEntity>>
    fun getDistinctNutritionForDateUntilChanged(date: LocalDate): Flow<List<NutritionEntity>> =
        getNutritionForDate(date).distinctUntilChanged()
}