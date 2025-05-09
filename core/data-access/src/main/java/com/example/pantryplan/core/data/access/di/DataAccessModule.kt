package com.example.pantryplan.core.data.access.di

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
    internal abstract fun bindUserPreferencesRepository(
        userPreferencesRepository: UserPreferencesRepositoryImpl
    ): UserPreferencesRepository
}