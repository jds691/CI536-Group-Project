package com.example.pantryplan.core.models

import kotlinx.datetime.Instant
import java.util.UUID
import kotlin.time.Duration

data class PantryItem(
    val id: UUID,
    val name: String,
    val quantity: Float,
    val expiryDate: Instant,
    // How long until an item will expire
    val expiresAfter: Duration?,
    // How long the item has been in its current state
    val inStateSince: Instant,
    val state: PantryItemState,
    val imageUrl: String?,
    val barcode: String?,
    val measurement: Measurement
)
