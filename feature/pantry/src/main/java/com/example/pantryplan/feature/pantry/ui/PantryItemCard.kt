package com.example.pantryplan.feature.pantry.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pantryplan.core.models.PantryItem
import com.example.pantryplan.core.models.PantryItemState
import com.example.pantryplan.feature.pantry.R
import java.time.LocalDate
import java.util.Calendar
import java.util.Date
import java.util.UUID

@Composable
fun PantryItemCard(item: PantryItem) {
    Column (
        verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.Top),
        horizontalAlignment = Alignment.Start,
    ) {
        Card(
            modifier = Modifier.fillMaxWidth().height(80.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxHeight(),
                horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.Start),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (item.imageUrl != null)
                {
                    //Place food item image in card when there is an active image passed in
                    //TODO
                }
                else
                {
                    Image(
                        modifier = Modifier.size(width = 80.dp, height = 80.dp),
                        painter = painterResource(R.drawable.beefburger),
                        contentDescription = null
                    )
                }
                Column (
                    verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.Top),
                    horizontalAlignment = Alignment.Start,
                ) {
                    Modifier.padding(top = 16.dp, bottom = 16.dp)
                    Text(
                        text = item.name,
                        style = TextStyle(
                            fontSize = 16.sp,
                            lineHeight = 24.sp,
                            fontWeight = FontWeight(500),
                            letterSpacing = 0.15.sp,
                        )
                    )
                    Text(
                        text = "Status",
                        style = TextStyle(
                            fontSize = 14.sp,
                            lineHeight = 20.sp,
                            fontWeight = FontWeight(400),
                            letterSpacing = 0.25.sp,
                        )
                    )
                }
            }

        }
    }
    
}

@Preview
@Composable
fun PantryItemCardPreview() {
    val foodItem:PantryItem = PantryItem(
        id = UUID.randomUUID(),
        name = "Cheese With Hat",
        quantity = 1000,
        expiryDate = TODO(),
        expiresAfter = 1,
        inStateSince = TODO(),
        state = PantryItemState.OPENED,
        imageUrl = null
    )
    PantryItemCard(item = foodItem)
}

fun CreateStatus(item: PantryItem)
{
    if (item.state != PantryItemState.FROZEN && item.state != PantryItemState.SEALED) //TODO: Add date check
    {
    }
}
