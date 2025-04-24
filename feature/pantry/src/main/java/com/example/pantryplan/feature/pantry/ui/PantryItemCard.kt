package com.example.pantryplan.feature.pantry.ui

import android.icu.text.RelativeDateTimeFormatter
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.example.pantryplan.core.models.PantryItem
import com.example.pantryplan.core.models.PantryItemState
import com.example.pantryplan.feature.pantry.R
import java.util.Date
import java.util.UUID
import kotlin.math.abs

fun dateDiffInDays(date1Millis: Long, date2Millis: Long): Long {
    val dateDiffMillis = date1Millis - date2Millis
    val seconds = dateDiffMillis / 1000
    val minutes = seconds / 60
    val hours = minutes / 60

    // Days
    return hours / 24
}

@Composable
fun PantryItemCard(item: PantryItem) {
    fun createStatus(): Pair<String, Color> {
        val formatter = RelativeDateTimeFormatter.getInstance()

        val days: Long = if (item.expiresAfter != null)
            dateDiffInDays(item.expiryDate.time + item.expiresAfter!!, item.expiryDate.time)
        else
        // Days since the expiry has passed
            dateDiffInDays(item.expiryDate.time, Date().time)

        return when (item.state) {
            PantryItemState.SEALED, PantryItemState.OPENED -> Pair(
                "Expires ${
                    formatter.format(
                        days.toDouble(),
                        RelativeDateTimeFormatter.Direction.NEXT,
                        RelativeDateTimeFormatter.RelativeUnit.DAYS
                    )
                }.",
                if (days <= 2) Color.Yellow else Color.Green
            )

            PantryItemState.FROZEN -> Pair(
                if (days == 0L) "Frozen today." else "Frozen ${
                    formatter.format(
                        abs(days.toDouble()),
                        RelativeDateTimeFormatter.Direction.LAST,
                        RelativeDateTimeFormatter.RelativeUnit.DAYS
                    )
                }.",
                Color.Cyan
            )

            PantryItemState.EXPIRED -> Pair(
                if (days == 0L) "Expired today." else "Expired ${
                    formatter.format(
                        abs(days.toDouble()),
                        RelativeDateTimeFormatter.Direction.LAST,
                        RelativeDateTimeFormatter.RelativeUnit.DAYS
                    )
                }.",
                Color.Red
            )
        }
    }

    val (status, statusColour) = createStatus()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxHeight(),
            horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.Start),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (item.imageUrl != null) {
                //TODO: Place food item image in card when there is an active image passed in
            } else {
                Image(
                    modifier = Modifier
                        .fillMaxHeight()
                        .aspectRatio(1.0f),
                    painter = painterResource(R.drawable.beefburger),
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
            }
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

@Preview
@Composable
fun PantryItemCardPreviews(@PreviewParameter(SamplePantryItemProvider::class) pantryItem: PantryItem) {
    PantryItemCard(item = pantryItem)
}

class SamplePantryItemProvider : PreviewParameterProvider<PantryItem> {
    override val values: Sequence<PantryItem> = sequenceOf(
        PantryItem(
            id = UUID.randomUUID(),
            name = "Cheese With Hat",
            quantity = 1000,
            expiryDate = Date(),
            expiresAfter = 86400 * 1000,
            inStateSince = Date(),
            state = PantryItemState.SEALED,
            imageUrl = null
        ),
        PantryItem(
            id = UUID.randomUUID(),
            name = "Cheese With Hat",
            quantity = 1000,
            expiryDate = Date(),
            expiresAfter = 86400 * 1000 * 3,
            inStateSince = Date(),
            state = PantryItemState.OPENED,
            imageUrl = null
        ),
        PantryItem(
            id = UUID.randomUUID(),
            name = "Cheese With Hat",
            quantity = 1000,
            expiryDate = Date(),
            expiresAfter = null,
            inStateSince = Date(),
            state = PantryItemState.FROZEN,
            imageUrl = null
        ),
        PantryItem(
            id = UUID.randomUUID(),
            name = "Cheese With Hat",
            quantity = 1000,
            expiryDate = Date(),
            expiresAfter = null,
            inStateSince = Date(),
            state = PantryItemState.EXPIRED,
            imageUrl = null
        )
    )
}
