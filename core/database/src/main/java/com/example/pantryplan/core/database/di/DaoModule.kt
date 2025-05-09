package com.example.pantryplan.core.database.di

import com.example.pantryplan.core.database.PantryPlanDatabase
import com.example.pantryplan.core.database.dao.PantryDao
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
}