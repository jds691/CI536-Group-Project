package com.example.pantryplan.core.data.access.di

import com.example.pantryplan.core.data.access.repository.NutritionRepository
import com.example.pantryplan.core.data.access.repository.NutritionRepositoryImpl
import com.example.pantryplan.core.data.access.repository.PantryItemRepository
import com.example.pantryplan.core.data.access.repository.PantryItemRepositoryImpl
import com.example.pantryplan.core.data.access.repository.PlannedMealsRepository
import com.example.pantryplan.core.data.access.repository.PlannedMealsRepositoryImpl
import com.example.pantryplan.core.data.access.repository.RecipeRepository
import com.example.pantryplan.core.data.access.repository.RecipeRepositoryImpl
import com.example.pantryplan.core.data.access.repository.UserPreferencesRepository
import com.example.pantryplan.core.data.access.repository.UserPreferencesRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataAccessModule {
    @Binds
    internal abstract fun bindPantryItemRepository(
        pantryItemRepository: PantryItemRepositoryImpl
    ): PantryItemRepository

    @Binds
    internal abstract fun bindRecipeRepository(
        recipeRepository: RecipeRepositoryImpl
    ): RecipeRepository

    @Binds
    internal abstract fun bindNutritionRepository(
        nutritionRepository: NutritionRepositoryImpl
    ): NutritionRepository

    @Binds
    internal abstract fun bindPlannedMealsRepository(
        plannedMealsRepository: PlannedMealsRepositoryImpl
    ): PlannedMealsRepository

    @Binds
    internal abstract fun bindUserPreferencesRepository(
        userPreferencesRepository: UserPreferencesRepositoryImpl
    ): UserPreferencesRepository
}