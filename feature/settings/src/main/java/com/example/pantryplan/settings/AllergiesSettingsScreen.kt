@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.pantryplan.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.pantryplan.core.designsystem.text.getDisplayNameId
import com.example.pantryplan.core.designsystem.theme.PantryPlanTheme
import com.example.pantryplan.core.models.Allergen
import java.util.EnumSet
import com.example.pantryplan.core.designsystem.R as designSystemR

@Composable
internal fun AllergiesSettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
    onBackClick: () -> Unit
) {
    val settingsUiState: SettingsUiState by viewModel.uiState.collectAsStateWithLifecycle()

    AllergiesSettingsScreen(
        uiState = settingsUiState,
        onBackClick = onBackClick,
        onUpdateAllergies = viewModel::updateAllergies,
        onUpdateIntolerances = viewModel::updateIntolerances
    )
}

@Composable
internal fun AllergiesSettingsScreen(
    uiState: SettingsUiState,
    onBackClick: () -> Unit,
    onUpdateAllergies: (EnumSet<Allergen>) -> Unit,
    onUpdateIntolerances: (EnumSet<Allergen>) -> Unit
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
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .consumeWindowInsets(innerPadding)
                .padding(innerPadding)
                .padding(horizontal = dimensionResource(designSystemR.dimen.horizontal_margin))
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            YourAllergies(
                uiState = uiState,
                onUpdateAllergies = onUpdateAllergies
            )

            HorizontalDivider()

            YourIntolerances(
                uiState = uiState,
                onUpdateIntolerances = onUpdateIntolerances
            )
        }
    }
}

@Composable
private fun YourAllergies(
    uiState: SettingsUiState,
    onUpdateAllergies: (EnumSet<Allergen>) -> Unit
) {
    Column {
        Text(
            text = "Your Allergies",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )

        val set = uiState.settings.allergies

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            for (allergy in Allergen.entries) {
                val selected = set.contains(allergy)
                FilterChip(
                    selected = selected,
                    onClick = {
                        if (selected)
                            set.remove(allergy)
                        else
                            set.add(allergy)

                        try {
                            onUpdateAllergies(EnumSet.copyOf(set))
                        } catch (e: IllegalArgumentException) {
                            // Thrown if set is empty when calling copyOf
                            onUpdateAllergies(EnumSet.noneOf(Allergen::class.java))
                        }
                    },
                    label = { Text(stringResource(allergy.getDisplayNameId())) },
                    leadingIcon = {
                        if (selected)
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(18.dp)
                            )
                    }
                )
            }
        }
    }
}

@Composable
private fun YourIntolerances(
    uiState: SettingsUiState,
    onUpdateIntolerances: (EnumSet<Allergen>) -> Unit
) {
    Column {
        Text(
            text = "Your Intolerances",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )

        val set = uiState.settings.intolerances

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            for (allergy in Allergen.entries) {
                val selected = set.contains(allergy)
                FilterChip(
                    selected = selected,
                    onClick = {
                        if (selected)
                            set.remove(allergy)
                        else
                            set.add(allergy)

                        try {
                            onUpdateIntolerances(EnumSet.copyOf(set))
                        } catch (e: IllegalArgumentException) {
                            onUpdateIntolerances(EnumSet.noneOf(Allergen::class.java))
                        }
                    },
                    label = { Text(stringResource(allergy.getDisplayNameId())) },
                    leadingIcon = {
                        if (selected)
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(18.dp)
                            )
                    }
                )
            }
        }
    }
}

@Preview
@Composable
private fun AllergiesSettingsScreenPreview() {
    PantryPlanTheme {
        AllergiesSettingsScreen(
            uiState = SettingsUiState(),
            onBackClick = {},
            onUpdateAllergies = {},
            onUpdateIntolerances = {}
        )
    }
}