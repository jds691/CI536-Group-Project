package com.example.pantryplan.core.designsystem.text

import androidx.annotation.StringRes
import com.example.pantryplan.core.designsystem.R
import com.example.pantryplan.core.models.Allergen
import com.example.pantryplan.core.models.Allergen.*

@StringRes
fun Allergen.getDisplayNameId(): Int {
    return when (this) {
        MILK -> R.string.feature_design_system_allergen_milk
        TREE_NUTS -> R.string.feature_design_system_allergen_tree_nuts
        GLUTEN -> R.string.feature_design_system_allergen_gluten
        EGGS -> R.string.feature_design_system_allergen_eggs
        PEANUTS -> R.string.feature_design_system_allergen_peanuts
        FISH -> R.string.feature_design_system_allergen_fish
        MOLLUSCS -> R.string.feature_design_system_allergen_molluscs
        LUPIN -> R.string.feature_design_system_allergen_lupin
        SESAME -> R.string.feature_design_system_allergen_sesame
        SOYBEANS -> R.string.feature_design_system_allergen_soybeans
        CELERY -> R.string.feature_design_system_allergen_celery
        MUSTARD -> R.string.feature_design_system_allergen_mustard
        SULPHIDES -> R.string.feature_design_system_allergen_sulphides
        CRUSTACEANS -> R.string.feature_design_system_allergen_crustaceans
    }
}