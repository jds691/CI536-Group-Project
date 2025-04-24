package com.example.pantryplan.core.models

import java.time.LocalDate
import java.util.Calendar
import java.util.Date
import java.util.UUID

enum class PantryItemState {
    SEALED, OPENED, FROZEN, EXPIRED
}

data class PantryItem(
    val id: UUID,
    val name: String,
    val quantity: Int,
    val expiryDate: LocalDate,
    // How long until an item will expire
    val expiresAfter: Int?,
    // How long the item has been in its current state
    val inStateSince: LocalDate,
    val state: PantryItemState,
    val imageUrl: String?
)
