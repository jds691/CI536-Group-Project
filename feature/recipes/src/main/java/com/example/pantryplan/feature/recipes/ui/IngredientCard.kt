@file:OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)

package com.example.pantryplan.feature.recipes.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pantryplan.feature.recipes.R

@Composable
fun IngredientCard(
    modifier: Modifier = Modifier,

    onClick: () -> Unit = {},
    onLongClick: () -> Unit = {},
    onDelete: () -> Unit = {},
) {
    //TODO Get name and gram amount specified when created
    val ingredientName = "American Cheese"
    val gramAmount = 20

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
                //if (null == false) {
                //TODO: Place recipe item image in card when there is an active image passed in
                //} else {
                Image(
                    modifier = Modifier
                        .fillMaxHeight()
                        .aspectRatio(1.0f),
                    painter = painterResource(R.drawable.americancheese),
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
                //}
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.Top),
                    horizontalAlignment = Alignment.Start,
                ) {


                    Text(
                        text = ingredientName,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "Uses: " + gramAmount + "g",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
        }
    }

    if (showDeleteAlert.value) {
        DeleteAlertDialog(
            itemName = ingredientName,
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

@Composable
internal fun DeleteAlertDialog(
    itemName: String,
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
                    contentDescription = "",
                    tint = AlertDialogDefaults.iconContentColor
                )

                Text(
                    text = "Delete '${itemName}'?",
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
fun IngredientCardPreview() {
    IngredientCard()
}
