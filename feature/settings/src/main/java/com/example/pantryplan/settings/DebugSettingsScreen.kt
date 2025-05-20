@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.pantryplan.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.pantryplan.core.designsystem.R
import com.example.pantryplan.core.designsystem.theme.PantryPlanTheme
import com.example.pantryplan.settings.ui.SettingsValueRow
import kotlin.system.exitProcess

@Composable
internal fun DebugSettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
    onBackClick: () -> Unit
) {
    val settingsUiState: SettingsUiState by viewModel.uiState.collectAsStateWithLifecycle()

    DebugSettingsScreen(
        uiState = settingsUiState,
        onBackClick = onBackClick,
    )
}

@Composable
internal fun DebugSettingsScreen(
    uiState: SettingsUiState,
    onBackClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            MediumTopAppBar(
                title = {
                    Text("Debug")
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
                .padding(horizontal = dimensionResource(R.dimen.horizontal_margin))
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            val context = LocalContext.current
            SettingsValueRow(
                title = "Reset Room",
                description = "Erases the database."
            ) {
                Button(
                    onClick = {
                        context.deleteDatabase("pantry-plan-database")
                        exitProcess(0)
                    },
                ) {
                    Text("Reset")
                }
            }

        }
    }
}

@Composable
private fun OutlinedIntField(
    value: Int,
    onValueChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    label: @Composable (() -> Unit)? = null,
) {
    OutlinedNumberField(
        value = value.toString(),
        onValueChange = {
            onValueChange(
                runCatching { it.toInt() }.getOrDefault(0)
            )
        },
        modifier = modifier,
        label = label,
    )
}

@Composable
private fun OutlinedNumberField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: @Composable (() -> Unit)? = null,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        label = label,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        singleLine = true,
    )
}

@Preview
@Composable
private fun MealPlannerSettingsScreenPreview() {
    PantryPlanTheme {
        DebugSettingsScreen(
            uiState = SettingsUiState(),
            onBackClick = {}
        )
    }
}