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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateSetOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pantryplan.core.designsystem.text.getDisplayNameId
import com.example.pantryplan.core.designsystem.theme.PantryPlanTheme
import com.example.pantryplan.core.models.Allergen
import com.example.pantryplan.core.models.Allergen.MILK
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
            verticalArrangement = Arrangement.spacedBy(4.dp),
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

        val set = remember { mutableStateSetOf(MILK) }

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
private fun YourIntolerances() {
    Column {
        Text(
            text = "Your Intolerances",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )

        val set = remember { mutableStateSetOf(MILK) }

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
            onBackClick = {}
        )
    }
}