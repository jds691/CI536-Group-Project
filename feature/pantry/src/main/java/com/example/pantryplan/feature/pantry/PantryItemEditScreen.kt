@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.pantryplan.feature.pantry

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.pantryplan.core.designsystem.component.ImageSelect
import java.util.UUID

@Composable
fun PantryItemEditScreen(
    existingId: UUID? = null,
    onBackClick: () -> Unit
) {
    Scaffold (
        topBar = {
            PantryItemEditTopBar(
                /* TODO: Pull pantry item name from the data layer. */
                itemName = existingId?.toString(),
                onBackClick = onBackClick
            )
        }
    ) { contentPadding ->
        Column (modifier = Modifier.padding(contentPadding)) {
            Text("Hello Pantry item edit!")
            ImageSelect(
                modifier = Modifier.fillMaxWidth(),
                onClick = { /* TODO: Actually add an image. */ },
            )
        }
    }
}

@Composable
fun PantryItemEditTopBar(itemName: String? = null, onBackClick: () -> Unit) {
    TopAppBar(
        title = {
            if (itemName == null) {
                Text("Add Item")
            } else {
                Text("Update ‘${itemName}’")
            }
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(Icons.Default.Clear, "")
            }
        },
        actions = {
            TextButton(
                onClick = { /* TODO: Actually save */ }
            ) {
                Text("Save")
            }
        }
    )
}
