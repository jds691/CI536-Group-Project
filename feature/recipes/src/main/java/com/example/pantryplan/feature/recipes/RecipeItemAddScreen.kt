package com.example.pantryplan.feature.recipes

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import java.util.UUID

@Composable
fun RecipeItemAddScreen(
    existingId: UUID? = null,
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(
            text = "Hello Recipe item add!"
        )

        Button(onClick = onBackClick) {
            Text("Back")
        }
    }
}
