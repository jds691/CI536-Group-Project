package com.example.pantryplan.core.designsystem.text

import androidx.annotation.StringRes
import com.example.pantryplan.core.designsystem.R
import com.example.pantryplan.core.models.PantryItemState
import com.example.pantryplan.core.models.PantryItemState.*

@StringRes
fun PantryItemState.getDisplayNameId(): Int {
    return when (this) {
        SEALED -> R.string.feature_design_system_pantry_item_state_sealed
        OPENED -> R.string.feature_design_system_pantry_item_state_opened
        FROZEN -> R.string.feature_design_system_pantry_item_state_frozen
        EXPIRED -> R.string.feature_design_system_pantry_item_state_expired
    }
}