package com.example.pantryplan.feature.pantry

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import java.util.UUID

@Composable
fun PantryItemEditScreen(
    existingId: UUID? = null,
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(
            text = "Hello Pantry item edit!"
        )

        Button(onClick = onBackClick) {
            Text("Back")
        }
    }
}
