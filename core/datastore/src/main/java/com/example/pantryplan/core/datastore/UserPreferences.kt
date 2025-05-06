package com.example.pantryplan.core.datastore

import com.example.pantryplan.core.models.Allergen
import java.util.EnumSet

data class UserPreferences(
    val allergies: EnumSet<Allergen>,
    val intolerances: EnumSet<Allergen>
)
