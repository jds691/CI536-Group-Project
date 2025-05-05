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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.pantryplan.core.designsystem.theme.PantryPlanTheme
import com.example.pantryplan.core.designsystem.R as designSystemR

@Composable
internal fun AllergiesSettingsScreen(
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            MediumTopAppBar(
                title = {
                    Text("Allergies & Intolerances")
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
                .padding(horizontal = dimensionResource(designSystemR.dimen.horizontal_margin))
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            YourAllergies()

            YourIntolerances()
        }
    }
}

@Composable
private fun YourAllergies() {
    Column {
        Text(
            text = "Your Allergies",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
private fun YourIntolerances() {
    Column {
        Text(
            text = "Your Intolerances",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Preview
@Composable
private fun AllergiesSettingsScreenPreview() {
    PantryPlanTheme {
        AllergiesSettingsScreen(
            onBackClick = {}
        )
    }
}