package com.example.pantryplan.core.datastore

import com.example.pantryplan.core.models.Allergen
import java.util.EnumSet
import kotlin.time.Duration

data class UserPreferences(
    val useRelativeDates: Boolean,
    val expiringSoonAmount: Duration,

    val allergies: EnumSet<Allergen>,
    val intolerances: EnumSet<Allergen>
)
