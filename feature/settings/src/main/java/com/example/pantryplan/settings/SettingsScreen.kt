@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.pantryplan.settings

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.pantryplan.core.designsystem.theme.PantryPlanTheme
import com.example.pantryplan.settings.ui.SettingsRow
import com.example.pantryplan.core.designsystem.R as designSystemR

@Composable
internal fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onAllergySettingsClick: () -> Unit,
    onPantrySettingsClick: () -> Unit,
    onMealPlannerSettingsClick: () -> Unit
) {
    val settingsUiState: SettingsUiState by viewModel.uiState.collectAsStateWithLifecycle()

    SettingsScreen(
        uiState = settingsUiState,
        onBackClick = onBackClick,
        onAllergySettingsClick = onAllergySettingsClick,
        onPantrySettingsClick = onPantrySettingsClick,
        onMealPlannerSettingsClick = onMealPlannerSettingsClick
    )
}


@Composable
internal fun SettingsScreen(
    uiState: SettingsUiState,
    onBackClick: () -> Unit,
    onAllergySettingsClick: () -> Unit,
    onPantrySettingsClick: () -> Unit,
    onMealPlannerSettingsClick: () -> Unit
) {
    Scaffold(
        topBar = {
            MediumTopAppBar(
                title = {
                    Text("Settings")
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
            modifier = Modifier
                .consumeWindowInsets(innerPadding)
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            SettingsRow(
                title = "Allergies & Intolerances",
                description = "${uiState.settings.allergies.count()} Allergies and Intolerances",
                onClick = onAllergySettingsClick
            )

            SettingsRow(
                title = "Pantry",
                description = "Pantry Settings",
                icon = designSystemR.drawable.feature_pantry_icon_outlined,
                onClick = onPantrySettingsClick
            )

            SettingsRow(
                title = "Meal Planner",
                description = "Meal Planner Settings",
                icon = designSystemR.drawable.feature_meal_planner_icon_outlined,
                onClick = onMealPlannerSettingsClick
            )
        }
    }
}

@Preview
@Composable
private fun SettingsScreenPreview() {
    PantryPlanTheme {
        SettingsScreen(
            uiState = SettingsUiState(),
            onBackClick = {},
            onAllergySettingsClick = {},
            onPantrySettingsClick = {},
            onMealPlannerSettingsClick = {}
        )
    }
}