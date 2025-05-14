package com.example.pantryplan.core.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.pantryplan.core.models.Measurement
import com.example.pantryplan.core.models.PantryItem
import com.example.pantryplan.core.models.PantryItemState
import kotlinx.datetime.Instant
import java.util.UUID
import kotlin.time.Duration

@Entity(tableName = "PantryStock")
data class PantryStock(
    @PrimaryKey
    val itemID: UUID,
    val itemName: String,
    val dateExpiring: Instant,
    val expiresAfter: Duration?,
    val quantity: Float,
    val inStateSince: Instant, // It just works ¯\_(ツ)_/¯
    val itemState: PantryItemState,
    val imageRefURL: String?, // TODO: Path once camera is functional
    val barcode: String?,
    val measurement: Measurement
)

fun PantryStock.asExternalModel() = PantryItem(
    id = itemID,
    name = itemName,
    quantity = quantity,
    expiryDate = dateExpiring,
    expiresAfter = expiresAfter,
    inStateSince = inStateSince,
    state = itemState,
    imageUrl = imageRefURL,
    barcode = barcode,
    measurement = measurement
)