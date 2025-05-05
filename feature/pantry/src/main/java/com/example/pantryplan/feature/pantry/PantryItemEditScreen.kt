@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.pantryplan.feature.pantry

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldLabelScope
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.pantryplan.core.designsystem.component.ImageSelect
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID
import com.example.pantryplan.core.designsystem.R as designSystemR

@Composable
fun PantryItemEditScreen(
    existingId: UUID? = null,
    onBackClick: () -> Unit
) {
    Scaffold (
        topBar = {
            PantryItemEditTopBar(
                /* TODO: Pull pantry item name from the data layer. */
                itemName = existingId?.toString(),
                onBackClick = onBackClick
            )
        },
    ) { contentPadding ->
        Box (modifier = Modifier.verticalScroll(rememberScrollState())) {
            Column(
                modifier = Modifier
                    .padding(contentPadding)
                    .consumeWindowInsets(contentPadding)
                    .padding(vertical = 8.dp)
            ) {
                Text(
                    text = "Image",
                    modifier = Modifier.padding(
                        horizontal = dimensionResource(designSystemR.dimen.form_horizontal_margin)
                    ),
                    color = MaterialTheme.colorScheme.outline,
                    style = MaterialTheme.typography.bodySmall,
                )
                ImageSelect(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                    onClick = { /* TODO: Actually add an image. */ },
                )
                PantryItemEditForm()
            }
        }
    }
}

@Composable
fun PantryItemEditTopBar(itemName: String? = null, onBackClick: () -> Unit) {
    TopAppBar(
        title = {
            if (itemName == null) {
                Text("Add Item")
            } else {
                Text("Update ‘${itemName}’")
            }
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(Icons.Default.Clear, "")
            }
        },
        actions = {
            TextButton(
                onClick = { /* TODO: Actually save */ }
            ) {
                Text("Save")
            }
        }
    )
}

@Composable
fun PantryItemEditForm() {
    Column(
        modifier = Modifier
            .padding(horizontal = dimensionResource(designSystemR.dimen.form_horizontal_margin)),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        var name by remember { mutableStateOf("") }

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Name") },
            singleLine = true,
        )

        OutlinedDatePickerField(
            label = { Text("Expires") },
            modifier = Modifier.fillMaxWidth(),
        )

        // TODO: Generate this from enum variants.
        val stateOptions = listOf("Sealed", "Opened", "Frozen", "Expired")
        OutlinedSelectField(
            modifier = Modifier.fillMaxWidth(),
            label = { Text("State") },
            options = stateOptions,
        )

        Text(
            text = "Quantity",
            color = MaterialTheme.colorScheme.outline,
            style = MaterialTheme.typography.bodySmall,
        )

        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedNumberField(modifier = Modifier.weight(1f))

            val measurementOptions = listOf("Grams", "Kilograms")
            OutlinedSelectField(
                modifier = Modifier.weight(1f),
                options = measurementOptions,
            )
        }

        Text(
            text = "When opened, expires in",
            color = MaterialTheme.colorScheme.outline,
            style = MaterialTheme.typography.bodySmall,
        )

        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedNumberField(modifier = Modifier.weight(1f))

            val timeOptions = listOf("Days", "Weeks", "Months")
            OutlinedSelectField(
                modifier = Modifier.weight(1f),
                options = timeOptions,
            )
        }
    }
}

@Composable
fun OutlinedDatePickerField(
    modifier: Modifier = Modifier,
    label: @Composable (() -> Unit)? = null,
) {
    var selectedDate by remember { mutableStateOf<Long?>(null) }
    var showModal by remember { mutableStateOf(false) }

    /* TODO: Remove this function, do actual date formatting with kotlin library. */
    fun convertMillisToDate(millis: Long): String {
        val formatter = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
        return formatter.format(Date(millis))
    }

    OutlinedTextField(
        value = selectedDate?.let { convertMillisToDate(it) } ?: "",
        onValueChange = { },
        modifier = modifier
            .pointerInput(selectedDate) {
                awaitEachGesture {
                    awaitFirstDown(pass = PointerEventPass.Initial)
                    val upEvent = waitForUpOrCancellation(pass = PointerEventPass.Initial)
                    if (upEvent != null) {
                        showModal = true
                    }
                }
            }
            .clickable(onClick = { showModal = true }),
        readOnly = true,
        label = label,
        trailingIcon = {
            Icon(Icons.Default.DateRange, contentDescription = "Select date")
        },
    )
    if (showModal) {
        DatePickerModal(
            onDateSelected = { selectedDate = it },
            onDismiss = { showModal = false }
        )
    }
}

@Composable
fun DatePickerModal(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()
    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(datePickerState.selectedDateMillis)
                onDismiss()
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
    ) {
        DatePicker(datePickerState)
    }
}

@Composable
fun OutlinedSelectField(
    modifier: Modifier = Modifier,
    label: @Composable (TextFieldLabelScope.() -> Unit)? = null,
    options: List<String>,
) {
    var expanded by remember { mutableStateOf(false) }
    val textFieldState = rememberTextFieldState(options[0])
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = modifier,
    ) {
        OutlinedTextField(
            readOnly = true,
            state = textFieldState,
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable),
            label = label,
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
                    }
                )
            }
        }
    }
}

// TODO: Input validation, ensure value is actually a number.
@Composable
fun OutlinedNumberField(
    modifier: Modifier = Modifier,
    label: @Composable (() -> Unit)? = null,
) {
    var quantity by remember { mutableStateOf("") }

    OutlinedTextField(
        value = quantity,
        onValueChange = { quantity = it },
        modifier = modifier,
        label = label,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        singleLine = true,
    )
}