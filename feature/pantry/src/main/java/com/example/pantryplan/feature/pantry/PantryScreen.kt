package com.example.pantryplan.feature.pantry

import androidx.compose.runtime.Composable
import com.example.pantryplan.core.models.PantryItem
import com.example.pantryplan.core.models.PantryItemState
import com.example.pantryplan.feature.pantry.ui.PantryItemCard
import java.util.Date
import java.util.UUID

@Composable
fun PantryScreen(
    onClickPantryItem: (UUID) -> Unit
) {
    val pantryItem = PantryItem(
        id = UUID.randomUUID(),
        name = "Cheese With Hat",
        quantity = 1000,
        expiryDate = Date(),
        expiresAfter = 86400 * 1000,
        inStateSince = Date(),
        state = PantryItemState.SEALED,
        imageUrl = null
    )

    PantryItemCard(
        item = pantryItem,
        onClick = {
            onClickPantryItem(pantryItem.id)
        }
    )
}
