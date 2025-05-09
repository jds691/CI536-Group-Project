package com.example.pantryplan.core.database.di

import android.content.Context
import androidx.room.Room
import com.example.pantryplan.core.database.PantryPlanDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DatabaseModule {
    @Provides
    @Singleton
    fun providesPantryPlanDatabase(
        @ApplicationContext context: Context,
    ): PantryPlanDatabase = Room.databaseBuilder(
        context,
        PantryPlanDatabase::class.java,
        "pantry-plan-database",
    ).build()
}
