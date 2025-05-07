@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.pantryplan.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.pantryplan.core.designsystem.R
import com.example.pantryplan.core.designsystem.theme.PantryPlanTheme
import com.example.pantryplan.settings.ui.SettingsValueRow
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days
import kotlin.time.DurationUnit

@Composable
internal fun PantrySettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
    onBackClick: () -> Unit
) {
    val settingsUiState: SettingsUiState by viewModel.uiState.collectAsStateWithLifecycle()

    PantrySettingsScreen(
        uiState = settingsUiState,
        onBackClick = onBackClick,
        onUpdateExpiringSoon = viewModel::updateExpiringSoonAmount,
        onChangeShowRelativeDates = viewModel::updateUseRelativeDates
    )
}

@Composable
internal fun PantrySettingsScreen(
    uiState: SettingsUiState,
    onBackClick: () -> Unit,
    onUpdateExpiringSoon: (Duration) -> Unit,
    onChangeShowRelativeDates: (Boolean) -> Unit
) {
    Scaffold(
        topBar = {
            MediumTopAppBar(
                title = {
                    Text("Pantry")
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
            SettingsValueRow(
                title = "Show Relative Dates",
                description =
                    if (uiState.settings.showRelativeDates.value) {
                        "Will show dates as 'Expires Tomorrow'."
                    } else {
                        val currentMoment = Clock.System.now()
                        val datetimeInSystemZone: LocalDateTime =
                            currentMoment.toLocalDateTime(TimeZone.currentSystemDefault())

                        val exactDateFormat = LocalDate.Format {
                            monthName(MonthNames.ENGLISH_ABBREVIATED); char(' '); dayOfMonth(); chars(
                            ", "
                        ); year()
                        }

                        "Will show dates as 'Expires ${
                            datetimeInSystemZone.date.format(
                                exactDateFormat
                            )
                        }'."
                    }
            ) {
                Switch(
                    checked = uiState.settings.showRelativeDates.value,
                    onCheckedChange = onChangeShowRelativeDates
                )
            }

            SettingsValueRow(
                "Expiring Soon Amount"
            ) {
                val timeOptions = listOf(
                    "1 Day",
                    "2 Days",
                    "3 Days",
                    "4 Days",
                    "5 Days",
                    "6 Days",
                    "1 Week"
                )
                OutlinedSelectField(
                    options = timeOptions,
                    modifier = Modifier
                        .width(125.dp),
                    // Reverse of updating duration
                    initialSelectedIndex = uiState.settings.expiringSoonAmount.value.toInt(
                        DurationUnit.DAYS
                    ) - 1,
                    onUpdate = {
                        val index = timeOptions.indexOf(it)

                        // Index is 0-6 so adding 1 gives 1-7 days
                        onUpdateExpiringSoon((index + 1).days)
                    }
                )
            }
        }
    }
}

@Composable
private fun OutlinedSelectField(
    modifier: Modifier = Modifier,
    options: List<String>,
    initialSelectedIndex: Int = 0,
    onUpdate: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val textFieldState = rememberTextFieldState(options[initialSelectedIndex])
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = modifier
    ) {
        OutlinedTextField(
            readOnly = true,
            state = textFieldState,
            modifier = Modifier
                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable),
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { selectionOption ->
                DropdownMenuItem(
                    text = { Text(text = selectionOption) },
                    onClick = {
                        textFieldState.setTextAndPlaceCursorAtEnd(selectionOption)
                        expanded = false
                        onUpdate(selectionOption)
                    }
                )
            }
        }
    }
}

@Preview
@Composable
private fun PantrySettingsScreenPreview() {
    PantryPlanTheme {
        PantrySettingsScreen(
            uiState = SettingsUiState(),
            onBackClick = {},
            onUpdateExpiringSoon = {},
            onChangeShowRelativeDates = {}
        )
    }
}