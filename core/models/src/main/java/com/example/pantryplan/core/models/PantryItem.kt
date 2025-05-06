package com.example.pantryplan.core.models

import kotlinx.datetime.Instant
import java.util.UUID

enum class PantryItemState {
    SEALED, OPENED, FROZEN, EXPIRED
}

data class PantryItem(
    val id: UUID,
    val name: String,
    val quantity: Int,
    val expiryDate: Instant,
    // How long until an item will expire
    val expiresAfter: Int?,
    // How long the item has been in its current state
    val inStateSince: Instant,
    val state: PantryItemState,
    val imageUrl: String?
)
