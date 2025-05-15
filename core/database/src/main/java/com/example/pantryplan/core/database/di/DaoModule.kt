package com.example.pantryplan.core.database.di

import com.example.pantryplan.core.database.PantryPlanDatabase
import com.example.pantryplan.core.database.dao.NutritionDao
import com.example.pantryplan.core.database.dao.PantryDao
import com.example.pantryplan.core.database.dao.PlannedMealsDao
import com.example.pantryplan.core.database.dao.RecipeDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal object DaoModule {
    @Provides
    fun providesPantryDao(
        database: PantryPlanDatabase,
    ): PantryDao = database.pantryDao()
    @Provides
    fun providesRecipeDao(
        database: PantryPlanDatabase,
    ): RecipeDao = database.recipeDao()

    @Provides
    fun providesNutritionDao(
        database: PantryPlanDatabase,
    ): NutritionDao = database.nutritionDao()

    @Provides
    fun providesPlannedMealsDao(
        database: PantryPlanDatabase,
    ): PlannedMealsDao = database.plannedMealsDao()
}