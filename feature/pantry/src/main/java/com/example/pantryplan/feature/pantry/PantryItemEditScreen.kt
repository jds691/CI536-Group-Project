@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalUuidApi::class)

package com.example.pantryplan.feature.pantry

import android.icu.text.SimpleDateFormat
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.activity.result.contract.ActivityResultContracts.TakePicture
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.rememberAsyncImagePainter
import com.example.pantryplan.core.designsystem.component.ImageSelect
import com.example.pantryplan.core.models.PantryItemState
import kotlinx.datetime.Instant
import java.util.Date
import java.util.Locale
import java.util.UUID
import kotlin.time.Duration.Companion.days
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid
import com.example.pantryplan.core.designsystem.R as designSystemR
import java.io.File
import kotlin.time.Duration

@Composable
fun PantryItemEditScreen(
    viewModel: PantryItemEditViewModel = hiltViewModel(),

    existingId: UUID? = null,
    barcode: String? = null,
    onBackClick: () -> Unit,
) {
    val pantryItemEditUiState by viewModel.uiState.collectAsStateWithLifecycle()
    PantryItemEditScreen(
        pantryItemEditUiState = pantryItemEditUiState,
        existingId = existingId,
        barcode = barcode,
        onBackClick = onBackClick,
        onSaveClick = viewModel::savePantryItem,
        onChangeName = viewModel::updateName,
        onChangeExpiryDate = viewModel::updateExpiryDate,
        onChangeState = viewModel::updateState,
        onChangeQuantity = viewModel::updateQuantity,
        onChangeExpiresAfter = viewModel::updateExpiresAfter
    )
}

@Composable
private fun PantryItemEditScreen(
    pantryItemEditUiState: PantryItemEditUiState,
    existingId: UUID?,
    barcode: String? = null,
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit,
    onChangeName: (String) -> Unit,
    onChangeExpiryDate: (Instant) -> Unit,
    onChangeState: (PantryItemState) -> Unit,
    onChangeQuantity: (Int) -> Unit,
    onChangeExpiresAfter: (Duration) -> Unit,
) {
    val pantryItem = pantryItemEditUiState.pantryItem
    Scaffold(
        modifier = Modifier.imePadding(),
        topBar = {
            PantryItemEditTopBar(
                // If we're editing an existing, then item show the name in the top bar.
                name = existingId?.let { pantryItem.name },
                onSaveClick = {
                    // Save and navigate to the previous screen.
                    onSaveClick()
                    onBackClick()
                },
                onBackClick = onBackClick,
            )
        },
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
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

            PantryItemImageSelect()
            PantryItemEditForm(
                name = pantryItem.name,
                expiryDate = pantryItem.expiryDate,
                state = pantryItem.state,
                quantity = pantryItem.quantity,
                expiresAfter = pantryItem.expiresAfter!!,
                onChangeName = onChangeName,
                onChangeExpiryDate = onChangeExpiryDate,
                onChangeState = onChangeState,
                onChangeQuantity = onChangeQuantity,
                onChangeExpiresAfter = onChangeExpiresAfter,
            )
        }
    }
}

@Composable
private fun PantryItemEditTopBar(
    name: String? = null,
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit,
) {
    TopAppBar(
        title = {
            if (name == null) {
                Text("Add Item")
            } else {
                Text("Update ‘${name}’")
            }
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(Icons.Default.Clear, "")
            }
        },
        actions = {
            TextButton(
                onClick = onSaveClick
            ) {
                Text("Save")
            }
        }
    )
}

@Composable
private fun PantryItemImageSelect() {
    var uri by remember { mutableStateOf<String?>(null) }

    val painter = rememberAsyncImagePainter(
        model = uri,
        fallback = painterResource(designSystemR.drawable.bigcheese),
    )

    val context = LocalContext.current

    // TODO: Move temporary file to persistent storage on form submit.
    // Generate random filename for the photo that the user takes/picks.
    val filename = Uuid.random().toString()
    val tempFile = File.createTempFile(filename, null, context.cacheDir)
    val tempFileUri = FileProvider.getUriForFile(
        context,
        context.packageName + ".provider",
        tempFile
    )
    val takePicture = rememberLauncherForActivityResult(TakePicture()) { success ->
        if (success) {
            uri = tempFileUri?.toString()
        }
    }

    val pickMedia = rememberLauncherForActivityResult(PickVisualMedia()) { picked ->
        picked?.let {
            context.contentResolver.openInputStream(it)!!.copyTo(tempFile.outputStream())
            uri = tempFileUri?.toString()
        }
    }

    ImageSelect(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
            .aspectRatio(1.8f),
        onTakePhoto = {
            takePicture.launch(tempFileUri)
        },
        onPickPhoto = {
            pickMedia.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))
        },
        backgroundPainter = painter
    )
}

@Composable
private fun PantryItemEditForm(
    name: String,
    expiryDate: Instant,
    state: PantryItemState,
    quantity: Int,
    expiresAfter: Duration,
    onChangeName: (String) -> Unit,
    onChangeExpiryDate: (Instant) -> Unit,
    onChangeState: (PantryItemState) -> Unit,
    onChangeQuantity: (Int) -> Unit,
    onChangeExpiresAfter: (Duration) -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(horizontal = dimensionResource(designSystemR.dimen.form_horizontal_margin)),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = name,
            onValueChange = onChangeName,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Name") },
            singleLine = true,
        )

        OutlinedDatePickerField(
            value = expiryDate.toEpochMilliseconds(),
            onValueChange = { expiryDateMillis ->
                onChangeExpiryDate(
                    Instant.fromEpochMilliseconds(expiryDateMillis!!)
                )
            },
            label = { Text("Expires") },
            modifier = Modifier.fillMaxWidth(),
        )

        val stateOptions = mapOf(
            PantryItemState.SEALED to "Sealed",
            PantryItemState.OPENED to "Opened",
            PantryItemState.FROZEN to "Frozen",
            PantryItemState.EXPIRED to "Expired",
        )
        OutlinedEnumSelectField(
            options = stateOptions,
            value = state,
            onValueChange = onChangeState,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("State") },
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
            // TODO: Replace with an enum string map when we swap to translation strings.
            val measurementOptions = listOf("Grams", "Kilograms")
            val measurementSelectState = rememberTextFieldState("Grams")

            OutlinedIntField(
                value = when (measurementSelectState.text) {
                    "Kilograms" -> quantity / 1000
                    else -> quantity
                },
                onValueChange = {
                    onChangeQuantity(
                        when (measurementSelectState.text) {
                            "Kilograms" -> it * 1000
                            else -> it
                        }
                    )
                },
                modifier = Modifier.weight(1f),
            )

            OutlinedSelectField(
                options = measurementOptions,
                textFieldState = measurementSelectState,
                modifier = Modifier.weight(1f),
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
            // TODO: Replace with an enum string map when we swap to translation strings.
            val timeOptions = listOf("Days", "Weeks", "Months")
            val timeSelectState = rememberTextFieldState("Days")

            OutlinedIntField(
                value = when (timeSelectState.text) {
                    // TODO: Replace with .weeks and .months when type is changed to DatePeriod.
                    "Weeks" -> expiresAfter.inWholeDays.toInt() / 7
                    "Months" -> expiresAfter.inWholeDays.toInt() / 30
                    else -> expiresAfter.inWholeDays.toInt()
                },
                onValueChange = {
                    onChangeExpiresAfter(
                        when (timeSelectState.text) {
                            "Weeks" -> it.days * 7
                            "Months" -> it.days * 30
                            else -> it.days
                        }
                    )
                },
                modifier = Modifier.weight(1f)
            )

            OutlinedSelectField(
                options = timeOptions,
                textFieldState = timeSelectState,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun OutlinedDatePickerField(
    value: Long?,
    onValueChange: (Long?) -> Unit,
    modifier: Modifier = Modifier,
    label: @Composable (() -> Unit)? = null,
) {
    var showModal by remember { mutableStateOf(false) }

    /* TODO: Remove this function, do actual date formatting with kotlin library. */
    fun convertMillisToDate(millis: Long): String {
        val formatter = SimpleDateFormat("EEE, dd MMM", Locale.getDefault())
        return formatter.format(Date(millis))
    }

    OutlinedTextField(
        value = value?.let { convertMillisToDate(it) } ?: "",
        onValueChange = { },
        modifier = modifier
            .pointerInput(value) {
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
            onDateSelected = onValueChange,
            onDismiss = { showModal = false },
            initialSelectedDateMillis = value,
        )
    }
}

@Composable
private fun DatePickerModal(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit,
    initialSelectedDateMillis: Long? = null,
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialSelectedDateMillis
    )
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
private fun <E: Enum<E>> OutlinedEnumSelectField(
    options: Map<E, String>,
    value: E,
    onValueChange: (E) -> Unit,
    modifier: Modifier = Modifier,
    label: @Composable (() -> Unit)? = null,
) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = modifier,
    ) {
        OutlinedTextField(
            readOnly = true,
            value = options[value]!!,
            onValueChange = {},
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
                    text = { Text(selectionOption.value) },
                    onClick = {
                        expanded = false
                        onValueChange(selectionOption.key)
                    }
                )
            }
        }
    }
}

@Composable
private fun OutlinedSelectField(
    options: List<String>,
    textFieldState: TextFieldState,
    modifier: Modifier = Modifier,
    label: @Composable (TextFieldLabelScope.() -> Unit)? = null,
) {
    var expanded by remember { mutableStateOf(false) }
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