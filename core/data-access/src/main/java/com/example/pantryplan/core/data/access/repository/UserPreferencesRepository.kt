package com.example.pantryplan.core.data.access.repository

import com.example.pantryplan.core.datastore.UserPreferences
import com.example.pantryplan.core.models.Allergen
import kotlinx.coroutines.flow.Flow
import java.util.EnumSet

interface UserPreferencesRepository {
    val preferences: Flow<UserPreferences>

    suspend fun setAllergies(allergies: EnumSet<Allergen>)
    suspend fun setIntolerances(intolerances: EnumSet<Allergen>)
}