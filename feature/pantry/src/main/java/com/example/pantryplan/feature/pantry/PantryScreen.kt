package com.example.pantryplan.feature.pantry

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import com.example.pantryplan.core.designsystem.component.ContentUnavailable
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Preview(widthDp = 400)
@Composable
fun PantryScreen(
    viewModel: PantryViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    if (uiState.value.pantryItems.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize().padding(16.dp, 0.dp),
            contentAlignment = Alignment.Center
        ) { PantryContentUnavailable() }
    }
}

@Composable
internal fun PantryContentUnavailable() {
    ContentUnavailable(
        icon = Icons.AutoMirrored.Filled.List,
        // TODO: Replace with translation strings.
        title = "No Items",
        description = "Your pantry is empty. Add some items to get started."
    )
}