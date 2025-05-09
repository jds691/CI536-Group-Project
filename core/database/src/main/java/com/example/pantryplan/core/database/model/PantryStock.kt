package com.example.pantryplan.core.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.Instant
import java.util.UUID

enum class PantryItemStates {
    FROZEN, UNSEALED, OPENED, EXPIRED
}

@Entity(tableName = "PantryStock")
data class PantryStock(
    @PrimaryKey val itemID: UUID,
    val itemName: String,
    val dateExpiring: Instant,
    val dateOpened: Instant?,
    val quantity: Int,
    val inStateSince: Instant, // It just works ¯\_(ツ)_/¯
    val itemState: PantryItemStates,
    val imageRefURL: String? // TODO: Path once camera is functional
)