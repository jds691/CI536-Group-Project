@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.pantryplan.feature.pantry

import android.icu.text.DecimalFormat
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.pantryplan.core.designsystem.text.asRelativeFormattedDate
import com.example.pantryplan.core.designsystem.text.pantryPlanExactFormat
import com.example.pantryplan.core.models.PantryItemState
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.util.UUID
import kotlin.time.Duration.Companion.days

fun getFormattedExpiryDate(expiryDate: Instant): String {
    val zone = TimeZone.currentSystemDefault()

    val localDateTime = expiryDate.toLocalDateTime(zone).date
    return localDateTime.pantryPlanExactFormat()
}

@Composable
fun PantryItemDetailsScreen(
    id: UUID,

    onEditItem: (UUID) -> Unit,
    onBackClick: () -> Unit,
    viewModel: PantryItemDetailsViewModel = hiltViewModel()
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    val item by viewModel.item.collectAsState()

    val formatter = DecimalFormat("0.#")

    LaunchedEffect(Unit) {
        viewModel.loadItem()
    }

    Scaffold(
        topBar = {
            MediumTopAppBar(
                title = {
                    Text(item.name)
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "")
                    }
                },
                actions = {
                    IconButton(onClick = {showDeleteDialog = true}) {
                        Icon(Icons.Outlined.Delete, "")
                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar(
                actions = {
                    Row {
                        if (item.state == PantryItemState.SEALED) {
                            //open
                            IconButton(onClick = {viewModel.updateState(PantryItemState.OPENED)}) {
                                Icon(painterResource(R.drawable.mark_as_opened), "Open")
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            //freeze
                            IconButton(onClick = {viewModel.updateState(PantryItemState.FROZEN)}) {
                                Icon(painterResource(R.drawable.freeze), "Freeze")
                            }
                        }
                        if (item.state == PantryItemState.OPENED) {
                            //freeze
                            IconButton(onClick = {viewModel.updateState(PantryItemState.FROZEN)}) {
                                Icon(painterResource(R.drawable.freeze), "Freeze")
                            }
                        }
                        if (item.state == PantryItemState.FROZEN) {
                            //unfreeze
                            IconButton(onClick = {
                                viewModel.updateState(PantryItemState.OPENED)
                                viewModel.updateExpiresAfter(1.days)
                            }) {
                                Icon(painterResource(R.drawable.unfreeze), "Unfreeze")
                            }
                        }
                    }
                },
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = {
                            onEditItem(id)
                        },
                        containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                        elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
                    ) {
                        Icon(Icons.Outlined.Edit, "")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .consumeWindowInsets(innerPadding)
                .padding(innerPadding)
        ) {
            Image(
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth(),
                painter = rememberAsyncImagePainter(
                    item.imageUrl,
                    fallback = painterResource(R.drawable.default_pantry_item_thumbnail)
                ),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )

            Column (
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row (
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text(
                        text = "Expires",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Row (
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = getFormattedExpiryDate(item.expiryDate),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                        Text(
                            text = " (${item.expiryDate.asRelativeFormattedDate()})",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
                HorizontalDivider()
                Row (
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text(
                        text = "State",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = item.state.toString().lowercase().replaceFirstChar { it.uppercase() },
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
                HorizontalDivider()
                Row (
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ){
                Text(
                    text = "Quantity",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text =if (item.quantity > 999) {
                        "${formatter.format(item.quantity/1000.0)}kg"
                    } else {
                        "${formatter.format(item.quantity)}g"
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
                if (showDeleteDialog) {
                    AlertDialog(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                        onDismissRequest = { showDeleteDialog = false },
                        title = { Text (
                            text = "Delete '${item.name}'?",
                            color = MaterialTheme.colorScheme.onSurface,
                            style = MaterialTheme.typography.headlineSmall
                        ) },
                        text = { Text (
                            text = "This item cannot be restored. Are you sure you want to delete it?",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        ) },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    showDeleteDialog = false
                                    viewModel.deleteItem()
                                    onBackClick()
                                }

                            ) {
                                Text(
                                    text = "Delete",
                                    color = MaterialTheme.colorScheme.primary,
                                    style = MaterialTheme.typography.labelLarge
                                )
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { showDeleteDialog = false }) {
                                Text(
                                    text ="Cancel",
                                    color = MaterialTheme.colorScheme.primary,
                                    style = MaterialTheme.typography.labelLarge
                                )

                            }
                        }
                    )
                }
            }
        }
    }
}