@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.pantryplan.feature.pantry

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import java.util.UUID

@Composable
fun PantryItemEditScreen(
    existingId: UUID? = null,
    onBackClick: () -> Unit
) {
    Scaffold (
        topBar = {
            TopAppBar(
                title = { Text("Add Item") },
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
        }) { contentPadding ->
        Column (modifier = Modifier.padding(contentPadding)) {
            Text(
                text = "Hello Pantry item edit!"
            )

            Button(onClick = onBackClick) {
                Text("Back")
            }
        }
    }
}
