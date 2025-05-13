@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.pantryplan.feature.recipes.ui

import android.icu.text.DecimalFormat
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pantryplan.core.designsystem.component.DeleteAlertDialog
import com.example.pantryplan.core.designsystem.theme.PantryPlanTheme
import com.example.pantryplan.core.models.Ingredient
import com.example.pantryplan.core.models.Measurement
import com.example.pantryplan.feature.recipes.R

@Composable
fun IngredientCard(
    modifier: Modifier = Modifier,
    ingredientData: Ingredient,

    onClick: () -> Unit = {},
    onLongClick: () -> Unit = {},
    onDelete: () -> Unit = {},
) {
    val showDeleteAlert = remember { mutableStateOf(false) }
    val isResetting = remember { mutableStateOf(false) }

    val measurementSignifier: String = when (ingredientData.measurement) {
        Measurement.GRAMS -> "g"
        Measurement.KILOGRAMS -> "kg"
        Measurement.OTHER -> ""
    }

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
            .height(72.dp)
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
                if (ingredientData.linkedPantryItem?.imageUrl != null) {
                    //TODO Pass in image url
                } else {
                Image(
                    modifier = Modifier
                        .fillMaxHeight()
                        .aspectRatio(1.0f),
                    painter = painterResource(R.drawable.americancheese),
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
                }
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.Top),
                    horizontalAlignment = Alignment.Start,
                ) {


                    Text(
                        text = ingredientData.name,
                        style = MaterialTheme.typography.titleMedium
                    )
                    val displayAmount = DecimalFormat("#.##")
                    Text(
                        text = "Uses: " + displayAmount.format(ingredientData.amount) + measurementSignifier,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
        }
    }

    if (showDeleteAlert.value) {
        DeleteAlertDialog(
            itemName = ingredientData.name,
            showAlert = showDeleteAlert,
            onDelete = onDelete
        )
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


@Preview
@Composable
fun IngredientCardPreview() {
    PantryPlanTheme {
        IngredientCard(
            modifier = Modifier,
            Ingredient(
                name = "American Cheese",
                amount = 200.5f,
                measurement = Measurement.GRAMS,
                linkedPantryItem = null
            )
        )
    }
}
