package com.example.pantryplan.core.data.access.repository

import com.example.pantryplan.core.datastore.UserPreferences
import com.example.pantryplan.core.datastore.UserPreferencesDataSource
import com.example.pantryplan.core.models.Allergen
import kotlinx.coroutines.flow.Flow
import java.util.EnumSet
import javax.inject.Inject
import kotlin.time.Duration

class UserPreferencesRepositoryImpl @Inject constructor(
    private val dataSource: UserPreferencesDataSource
) : UserPreferencesRepository {
    override val preferences: Flow<UserPreferences> = dataSource.preferences

    override suspend fun setUseRelativeDates(use: Boolean) {
        dataSource.setUseRelativeDates(use)
    }

    override suspend fun setExpiringSoonAmount(amount: Duration) {
        dataSource.setExpiringSoonAmount(amount)
    }

    override suspend fun setAllergies(allergies: EnumSet<Allergen>) {
        dataSource.setAllergies(allergies)
    }

    override suspend fun setIntolerances(intolerances: EnumSet<Allergen>) {
        dataSource.setIntolerances(intolerances)
    }
}