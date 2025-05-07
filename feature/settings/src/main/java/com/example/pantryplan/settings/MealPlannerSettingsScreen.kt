@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.pantryplan.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.pantryplan.core.designsystem.theme.PantryPlanTheme
import com.example.pantryplan.core.designsystem.R as designSystemR

@Composable
internal fun MealPlannerSettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
    onBackClick: () -> Unit
) {
    val settingsUiState: SettingsUiState by viewModel.uiState.collectAsStateWithLifecycle()

    MealPlannerSettingsScreen(
        uiState = settingsUiState,
        onBackClick = onBackClick
    )
}

@Composable
internal fun MealPlannerSettingsScreen(
    uiState: SettingsUiState,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            MediumTopAppBar(
                title = {
                    Text("Meal Planner")
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .consumeWindowInsets(innerPadding)
                .padding(innerPadding)
                .padding(horizontal = dimensionResource(designSystemR.dimen.horizontal_margin))
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
        }
    }
}

@Preview
@Composable
private fun MealPlannerSettingsScreenPreview() {
    PantryPlanTheme {
        MealPlannerSettingsScreen(
            uiState = SettingsUiState(),
            onBackClick = {}
        )
    }
}