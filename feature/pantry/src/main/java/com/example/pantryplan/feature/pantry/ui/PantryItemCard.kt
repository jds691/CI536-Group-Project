@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.pantryplan.feature.pantry.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.pantryplan.core.designsystem.text.asRelativeFormattedDate
import com.example.pantryplan.core.designsystem.theme.PantryPlanTheme
import com.example.pantryplan.core.models.Measurement
import com.example.pantryplan.core.models.PantryItem
import com.example.pantryplan.core.models.PantryItemState
import com.example.pantryplan.feature.pantry.R
import kotlinx.coroutines.delay
import kotlinx.datetime.Clock
import java.util.UUID
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days

@Composable
fun createStatus(
    item: PantryItem,
    expiringSoonDuration: Duration
): Pair<String, Color> {
    // TODO: When Theme.kt is updated to support the extended colours, replace the status colour

    val now = Clock.System.now()
    return when (item.state) {
        PantryItemState.SEALED, PantryItemState.OPENED -> {
            val expiry = item.expiryDate
            val timestamp = expiry.asRelativeFormattedDate()

            val expiringSoon = (expiry - now) <= expiringSoonDuration
            val color =
                if (expiringSoon) PantryPlanTheme.colorScheme.itemStatusExpiringSoon.color else PantryPlanTheme.colorScheme.itemStatusOk.color

            Pair("Expires ${timestamp}.", color)
        }

        PantryItemState.FROZEN -> {
            val frozen = item.inStateSince
            val timestamp = frozen.asRelativeFormattedDate()

            Pair("Frozen ${timestamp}.", PantryPlanTheme.colorScheme.itemStatusFrozen.color)
        }

        PantryItemState.EXPIRED -> {
            val expiry = item.expiryDate
            val timestamp = expiry.asRelativeFormattedDate()

            Pair("Expired ${timestamp}.", PantryPlanTheme.colorScheme.itemStatusExpired.color)
        }
    }
}

@Composable
fun PantryItemCard(
    item: PantryItem,
    expiringSoonDuration: Duration,
    modifier: Modifier = Modifier,

    onClick: () -> Unit = {},
    onLongClick: () -> Unit = {},
    onDelete: () -> Unit = {},
) {
    val (status, statusColour) = createStatus(item, expiringSoonDuration)

    var refreshToggle by remember { mutableStateOf(false) }

    val showDeleteAlert = remember { mutableStateOf(false) }
    val isResetting = remember { mutableStateOf(false) }

    val dismissState = rememberSwipeToDismissBoxState()
    SwipeToDismissBox(
        state = dismissState,
        enableDismissFromStartToEnd = false,
        backgroundContent = {
            val backgroundColour by animateColorAsState(
                when (dismissState.targetValue) {
                    SwipeToDismissBoxValue.Settled -> MaterialTheme.colorScheme.surface
                    SwipeToDismissBoxValue.EndToStart -> Color.Red
                    else -> Color.Unspecified
                }
            )

            val iconColour by animateColorAsState(
                when (dismissState.targetValue) {
                    SwipeToDismissBoxValue.Settled -> MaterialTheme.colorScheme.surface
                    SwipeToDismissBoxValue.EndToStart -> Color.White
                    else -> Color.Unspecified
                }
            )

            Box(
                Modifier
                    .fillMaxSize()
                    .background(
                        color = backgroundColour,
                        shape = CardDefaults.elevatedShape
                    )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = iconColour
                    )
                }
            }
        },
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
    ) {
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .combinedClickable(
                    onClick = onClick,
                    onLongClick = onLongClick
                )
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.Start),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = item.imageUrl,
                    modifier = Modifier
                        .fillMaxHeight()
                        .aspectRatio(1.0f),
                    fallback = painterResource(R.drawable.default_pantry_item_thumbnail),
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.Top),
                    horizontalAlignment = Alignment.Start,
                ) {
                    Text(
                        text = item.name,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = status,
                        style = MaterialTheme.typography.bodyMedium,
                        color = statusColour
                    )
                }
            }
        }
    }

    if (showDeleteAlert.value) {
        DeleteAlertDialog(
            item = item,
            showAlert = showDeleteAlert,
            onDelete = onDelete
        )
    }

    // Refreshes the UI every minute to have an accurate status number
    LaunchedEffect(refreshToggle) {
        delay(1_000 * 60) // 1 Minute
        refreshToggle = !refreshToggle
    }

    LaunchedEffect(isResetting.value) {
        if (isResetting.value) {
            // Reset is supposed to be called in a LaunchedEffect
            dismissState.reset()
            isResetting.value = false
        }
    }

    // If card is not at the start, but the card is fully in one direction e.g. SwipeToDismissBoxValue.EndToStart
    // Effectively checks if the card needs to be reset to the center
    val swipeNeedsReset =
        dismissState.currentValue != SwipeToDismissBoxValue.Settled && dismissState.progress == 1.0f

    if (!isResetting.value && !showDeleteAlert.value && swipeNeedsReset) {
        isResetting.value = true
    }

    // If card has been fully slided to the end
    val slideFinished =
        dismissState.targetValue == SwipeToDismissBoxValue.EndToStart && dismissState.progress == 1.0f

    if (!isResetting.value && !showDeleteAlert.value && slideFinished) {
        showDeleteAlert.value = true
    }
}

@Composable
internal fun DeleteAlertDialog(
    item: PantryItem,
    showAlert: MutableState<Boolean>,
    onDelete: () -> Unit
) {
    BasicAlertDialog(
        onDismissRequest = {
            showAlert.value = false
        }
    ) {
        Surface(
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight(),
            shape = AlertDialogDefaults.shape,
            tonalElevation = AlertDialogDefaults.TonalElevation,
            color = AlertDialogDefaults.containerColor
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement
                    .spacedBy(
                        space = 16.dp,
                        alignment = Alignment.CenterVertically
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null,
                    tint = AlertDialogDefaults.iconContentColor
                )

                Text(
                    text = "Delete '${item.name}'?",
                    color = AlertDialogDefaults.titleContentColor,
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    text = "This item cannot be restored. Are you sure you want to delete it?",
                    color = AlertDialogDefaults.textContentColor,
                    style = MaterialTheme.typography.bodyMedium
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = {
                        showAlert.value = false
                    }) { Text("Cancel") }

                    TextButton(onClick = {
                        showAlert.value = false
                        onDelete()
                    }) { Text("Delete") }
                }
            }
        }
    }
}

@Preview
@Composable
fun PantryItemCardPreviews(@PreviewParameter(SamplePantryItemProvider::class) pantryItem: PantryItem) {
    PantryPlanTheme {
        PantryItemCard(
            item = pantryItem,
            expiringSoonDuration = 2.days
        )
    }
}

class SamplePantryItemProvider : PreviewParameterProvider<PantryItem> {
    override val values: Sequence<PantryItem> = sequenceOf(
        PantryItem(
            id = UUID.randomUUID(),
            name = "Cheese With Hat",
            quantity = 1000f,
            expiryDate = Clock.System.now().plus(3.days),
            expiresAfter = 1.days,
            inStateSince = Clock.System.now(),
            state = PantryItemState.SEALED,
            imageUrl = null,
            barcode = null,
            measurement = Measurement.GRAMS
        ),
        PantryItem(
            id = UUID.randomUUID(),
            name = "Cheese With Hat",
            quantity = 1000f,
            expiryDate = Clock.System.now().plus(1.days),
            expiresAfter = 3.days,
            inStateSince = Clock.System.now(),
            state = PantryItemState.OPENED,
            imageUrl = null,
            barcode = null,
            measurement = Measurement.GRAMS
        ),
        PantryItem(
            id = UUID.randomUUID(),
            name = "Cheese With Hat",
            quantity = 1000f,
            expiryDate = Clock.System.now(),
            expiresAfter = null,
            inStateSince = Clock.System.now().minus(1.days),
            state = PantryItemState.FROZEN,
            imageUrl = null,
            barcode = null,
            measurement = Measurement.GRAMS
        ),
        PantryItem(
            id = UUID.randomUUID(),
            name = "Cheese With Hat",
            quantity = 1000f,
            expiryDate = Clock.System.now().minus(1.days),
            expiresAfter = null,
            inStateSince = Clock.System.now(),
            state = PantryItemState.EXPIRED,
            imageUrl = null,
            barcode = null,
            measurement = Measurement.GRAMS
        )
    )
}
