@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalUuidApi::class)

package com.example.pantryplan.feature.recipes

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.activity.result.contract.ActivityResultContracts.TakePicture
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.AssistChip
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.rememberAsyncImagePainter
import com.example.pantryplan.core.designsystem.R
import com.example.pantryplan.core.designsystem.component.ImageSelect
import com.example.pantryplan.core.designsystem.text.getDisplayNameId
import com.example.pantryplan.core.designsystem.theme.PantryPlanTheme
import com.example.pantryplan.core.models.Allergen
import com.example.pantryplan.core.models.Ingredient
import com.example.pantryplan.core.models.Measurement
import com.example.pantryplan.core.models.NutritionInfo
import com.example.pantryplan.feature.recipes.ui.IngredientCard
import java.io.File
import java.util.EnumSet
import java.util.UUID
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid


@Composable
fun RecipeItemAddScreen(
    viewModel: RecipeItemAddViewModel = hiltViewModel(),

    existingId: UUID? = null,
    onBackClick: () -> Unit,
) {
    val recipeItemAddUiState by viewModel.uiState.collectAsStateWithLifecycle()
    RecipeItemAddScreen(
        recipeItemAddUiState = recipeItemAddUiState,
        existingId = existingId,
        onBackClick = onBackClick,
        onSaveClick = viewModel::saveRecipeItem,
        onChangeName = viewModel::updateName,
        onChangeDescription = viewModel::updateDescription,
        onChangeTags = viewModel::updateTags,
        onRemoveTags = viewModel::removeTags,
        onChangeAllergens = viewModel::updateAllergens,
        onChangeInstructions = viewModel::updateInstructions,
        onRemoveInstructions = viewModel::removeInstructions,
        onChangeIngredients = viewModel::updateIngredients,
        onChangePrepTime = viewModel::updatePrepTime,
        onChangeCookTime = viewModel::updateCookTime,
        onChangeNutritionalInfo = viewModel::updateNutritionalInfo,
    )
}


@Composable
private fun RecipeItemEditTopBar(
    name: String? = null,
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit,

    ) {
    TopAppBar(
        title = {
            if (name == null) {
                Text("Add Recipe")
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
private fun RecipeItemImageSelect() {
    var uri by remember { mutableStateOf<String?>(null) }

    val painter = rememberAsyncImagePainter(
        model = uri,
        fallback = painterResource(R.drawable.cheeseburger),
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
private fun OutlinedFloatField(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    label: @Composable (() -> Unit)? = null,
) {
    OutlinedNumberField(
        value = value.toString(),
        onValueChange = {
            onValueChange(
                runCatching { it.toFloat() }.getOrDefault(0f)
            )
        },
        modifier = modifier,
        label = label,
    )
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

@Composable
private fun <E : Enum<E>> OutlinedEnumSelectField(
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
fun RecipeItemAddScreen(

    recipeItemAddUiState: RecipeItemAddUiState,
    existingId: UUID? = null,
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit,

    onChangeName: (String) -> Unit,
    onChangeDescription: (String) -> Unit,
    onChangeTags: (String) -> Unit,
    onRemoveTags: (String) -> Unit,
    onChangeAllergens: (EnumSet<Allergen>) -> Unit,
    onChangeInstructions: (String) -> Unit,
    onRemoveInstructions: (String) -> Unit,
    onChangeIngredients: (Ingredient) -> Unit,
    onChangePrepTime: (Float) -> Unit,
    onChangeCookTime: (Float) -> Unit,
    onChangeNutritionalInfo: (NutritionInfo) -> Unit,
) {

    val recipeItem = recipeItemAddUiState.recipeItem
    Scaffold(
        modifier = Modifier.imePadding(),
        topBar = {
            RecipeItemEditTopBar(
                // If we're editing an existing, then item show the name in the top bar.
                name = existingId?.let { recipeItem.title },
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
                    horizontal = dimensionResource(R.dimen.form_horizontal_margin)
                ),
                color = MaterialTheme.colorScheme.outline,
                style = MaterialTheme.typography.bodySmall,
            )

            RecipeItemImageSelect()
            RecipeItemEditForm(
                name = recipeItem.title,
                description = recipeItem.description,
                tags = recipeItem.tags,
                allergens = recipeItem.allergens,
                instructions = recipeItem.instructions,
                ingredients = recipeItem.ingredients,
                prepTime = recipeItem.prepTime,
                cookTime = recipeItem.cookTime,
                nutrition = recipeItem.nutrition,

                onChangeName = onChangeName,
                onChangeDescription = onChangeDescription,
                onChangeTags = onChangeTags,
                onRemoveTags = onRemoveTags,
                onChangeAllergens = onChangeAllergens,
                onChangeInstructions = onChangeInstructions,
                onChangeIngredients = onChangeIngredients,
                onRemoveInstructions = onRemoveInstructions,
                onChangePrepTime = onChangePrepTime,
                onChangeCookTime = onChangeCookTime,
                onChangeNutritionalInfo = onChangeNutritionalInfo,
            )
        }

    }
}

@Composable
private fun RecipeItemEditForm(
    name: String,
    description: String,
    tags: List<String>,
    allergens: EnumSet<Allergen>,
    instructions: List<String>,
    ingredients: List<Ingredient>,
    prepTime: Float,
    cookTime: Float,
    nutrition: NutritionInfo,

    onChangeName: (String) -> Unit,
    onChangeDescription: (String) -> Unit,
    onChangeTags: (String) -> Unit,
    onRemoveTags: (String) -> Unit,
    onChangeAllergens: (EnumSet<Allergen>) -> Unit,
    onChangeInstructions: (String) -> Unit,
    onRemoveInstructions: (String) -> Unit,
    onChangeIngredients: (Ingredient) -> Unit,
    onChangePrepTime: (Float) -> Unit,
    onChangeCookTime: (Float) -> Unit,
    onChangeNutritionalInfo: (NutritionInfo) -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(horizontal = dimensionResource(R.dimen.form_horizontal_margin)),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        OutlinedTextField(
            value = name,
            onValueChange = onChangeName,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Name") },
            singleLine = true,
            trailingIcon = {
                IconButton(
                    onClick = { onChangeName("") }
                ) {
                    Icon(Icons.Default.Clear, contentDescription = "Clear Field")
                }
            }
        )

        OutlinedTextField(
            value = description,
            onValueChange = onChangeDescription,
            label = { Text("Description") },
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp),
            trailingIcon = {
                IconButton(
                    onClick = { onChangeDescription("") }
                ) {
                    Icon(Icons.Default.Clear, contentDescription = "Clear Field")
                }
            }
        )


        Text(
            text = "Allergens",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleMedium,
        )

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val allergenSet = allergens
            for (allergy in Allergen.entries) {
                val selected = allergenSet.contains(allergy)
                FilterChip(
                    selected = selected,
                    onClick = {
                        if (selected)
                            allergenSet.remove(allergy)
                        else
                            allergenSet.add(allergy)

                        try {
                            onChangeAllergens(EnumSet.copyOf(allergenSet))
                        } catch (e: IllegalArgumentException) {
                            // Thrown if set is empty when calling copyOf
                            onChangeAllergens(EnumSet.noneOf(Allergen::class.java))
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

        HorizontalDivider(
            modifier = Modifier
                .padding(0.dp, 4.dp, 0.dp, 4.dp)
        )

        Text(
            text = "Recipe Time",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleMedium,
        )

        OutlinedFloatField(
            value = prepTime,
            onValueChange = onChangePrepTime,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Prep Time (Minutes)") },
        )

        OutlinedFloatField(
            value = cookTime,
            onValueChange = onChangeCookTime,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Cook Time (Minutes)") },
        )

        Text(
            text = "Tags",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleMedium,
        )

        var tagText by remember { mutableStateOf("") }
        OutlinedTextField(
            value = tagText,
            onValueChange = { tagText = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Tag") },
            singleLine = true,
            trailingIcon = {
                IconButton(
                    onClick = { tagText = "" }
                ) {
                    Icon(Icons.Default.Clear, contentDescription = "Clear Field")
                }
            }
        )

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            OutlinedButton(
                onClick = {
                    if (tagText != "") {
                        onChangeTags(tagText)
                    }
                },
                shape = ButtonDefaults.outlinedShape,
                enabled = true,
                colors = ButtonColors(
                    containerColor = Color.Unspecified,
                    contentColor = MaterialTheme.colorScheme.primary,
                    disabledContainerColor = Color.Unspecified,
                    disabledContentColor = MaterialTheme.colorScheme.error
                ),
                content = {
                    Icon(
                        Icons.Filled.Add,
                        contentDescription = ""
                    )
                    Spacer(Modifier.width(2.dp))
                    Text("Add Tag")
                }
            )
            for (tag in tags) {
                AssistChip(
                    onClick = { onRemoveTags(tag) },
                    label = { Text(tag) },
                )
            }

        }

        HorizontalDivider(
            modifier = Modifier
                .padding(0.dp, 4.dp, 0.dp, 4.dp)
        )

        var ingredientText by remember { mutableStateOf("") }
        var quantityAmount by remember { mutableStateOf(20f) }
        var measurementOption by remember { mutableStateOf(Measurement.GRAMS) }

        OutlinedTextField(
            value = ingredientText,
            onValueChange = { ingredientText = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Ingredient Name") },
            singleLine = true,
            trailingIcon = {
                IconButton(
                    onClick = { ingredientText = "" }
                ) {
                    Icon(Icons.Default.Clear, contentDescription = "Clear Field")
                }
            }
        )

        Text(
            text = "Quantity",
            color = MaterialTheme.colorScheme.outline,
            style = MaterialTheme.typography.bodySmall,
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.Start),
            verticalAlignment = Alignment.CenterVertically
        ) {

            OutlinedFloatField(
                value = quantityAmount,
                onValueChange = { quantityAmount = it },
                modifier = Modifier
                    .width(180.dp)
            )

            val measurementOptions = mapOf(
                Measurement.GRAMS to "Grams",
                Measurement.KILOGRAMS to "Kilograms",
                Measurement.OTHER to "Other"
            )

            OutlinedEnumSelectField(
                options = measurementOptions,
                value = measurementOption,
                onValueChange = { measurementOption = it },
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.Bottom)
            )
        }

        OutlinedButton(
            onClick = {
                onChangeIngredients(
                    Ingredient(
                        name = ingredientText,
                        amount = quantityAmount,
                        measurement = measurementOption,
                        linkedPantryItem = null
                    )
                )
            },
            shape = ButtonDefaults.outlinedShape,
            enabled = true,
            colors = ButtonColors(
                containerColor = Color.Unspecified,
                contentColor = MaterialTheme.colorScheme.primary,
                disabledContainerColor = Color.Unspecified,
                disabledContentColor = MaterialTheme.colorScheme.error
            ),
            content = {
                Icon(
                    Icons.Filled.Add,
                    contentDescription = ""
                )
                Spacer(Modifier.width(2.dp))
                Text("Add Ingredient")
            }
        )

        Text(
            text = "Ingredients",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleMedium,
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Top),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            ingredients.forEach { ingredient ->
                IngredientCard(
                    modifier = Modifier,
                    Ingredient(
                        name = ingredient.name,
                        amount = ingredient.amount,
                        measurement = ingredient.measurement,
                        linkedPantryItem = ingredient.linkedPantryItem
                    ),
                )
            }
        }

        HorizontalDivider(
            modifier = Modifier
                .padding(0.dp, 4.dp, 0.dp, 4.dp)
        )

        var stepText by remember { mutableStateOf("") }
        OutlinedTextField(
            value = stepText,
            onValueChange = { stepText = it },
            label = { Text("Step") },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            trailingIcon = {
                IconButton(
                    onClick = { stepText = "" }
                ) {
                    Icon(Icons.Default.Clear, contentDescription = "Clear Field")
                }
            }
        )

        OutlinedButton(
            onClick = {
                if (stepText != "") {
                    onChangeInstructions(stepText)
                }
            },
            shape = ButtonDefaults.outlinedShape,
            enabled = true,
            colors = ButtonColors(
                containerColor = Color.Unspecified,
                contentColor = MaterialTheme.colorScheme.primary,
                disabledContainerColor = Color.Unspecified,
                disabledContentColor = MaterialTheme.colorScheme.error
            ),
            content = {
                Icon(
                    Icons.Filled.Add,
                    contentDescription = ""
                )
                Spacer(Modifier.width(2.dp))
                Text("Add Step")
            }
        )

        Text(
            text = "Instructions",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleMedium,
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Top),
            horizontalAlignment = Alignment.Start
        ) {
            var stepNum = 1
            instructions.forEach { instruction ->
                Text(
                    text = "Step $stepNum - $instruction",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .clickable {
                            onRemoveInstructions(instruction)
                        }
                )
                stepNum++
            }
        }

        HorizontalDivider(
            modifier = Modifier
                .padding(0.dp, 4.dp, 0.dp, 4.dp)
        )

        Text(
            text = "Nutritional Information",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleMedium,
        )

        OutlinedIntField(
            value = nutrition.calories,
            onValueChange = {
                onChangeNutritionalInfo(
                    NutritionInfo(
                        calories = it,
                        fats = nutrition.fats,
                        saturatedFats = nutrition.saturatedFats,
                        carbohydrates = nutrition.carbohydrates,
                        sugar = nutrition.sugar,
                        fiber = nutrition.fiber,
                        protein = nutrition.protein,
                        sodium = nutrition.sodium
                    )
                )
            },
            modifier = Modifier
                .fillMaxWidth(),
            label = { Text("Calories") },
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedFloatField(
                value = nutrition.fats,
                onValueChange = {
                    onChangeNutritionalInfo(
                        NutritionInfo(
                            calories = nutrition.calories,
                            fats = it,
                            saturatedFats = nutrition.saturatedFats,
                            carbohydrates = nutrition.carbohydrates,
                            sugar = nutrition.sugar,
                            fiber = nutrition.fiber,
                            protein = nutrition.protein,
                            sodium = nutrition.sodium
                        )
                    )
                },
                modifier = Modifier.width(160.dp),
                label = { Text("Fat") },
            )
            OutlinedFloatField(
                value = nutrition.saturatedFats,
                onValueChange = {
                    onChangeNutritionalInfo(
                        NutritionInfo(
                            calories = nutrition.calories,
                            fats = nutrition.fats,
                            saturatedFats = it,
                            carbohydrates = nutrition.carbohydrates,
                            sugar = nutrition.sugar,
                            fiber = nutrition.fiber,
                            protein = nutrition.protein,
                            sodium = nutrition.sodium
                        )
                    )
                },
                modifier = Modifier.width(160.dp),
                label = { Text("Saturated Fats") },
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedFloatField(
                value = nutrition.carbohydrates,
                onValueChange = {
                    onChangeNutritionalInfo(
                        NutritionInfo(
                            calories = nutrition.calories,
                            fats = nutrition.fats,
                            saturatedFats = nutrition.saturatedFats,
                            carbohydrates = it,
                            sugar = nutrition.sugar,
                            fiber = nutrition.fiber,
                            protein = nutrition.protein,
                            sodium = nutrition.sodium
                        )
                    )
                },
                modifier = Modifier.width(160.dp),
                label = { Text("Carbohydrate") },
            )
            OutlinedFloatField(
                value = nutrition.sugar,
                onValueChange = {
                    onChangeNutritionalInfo(
                        NutritionInfo(
                            calories = nutrition.calories,
                            fats = nutrition.fats,
                            saturatedFats = nutrition.saturatedFats,
                            carbohydrates = nutrition.carbohydrates,
                            sugar = it,
                            fiber = nutrition.fiber,
                            protein = nutrition.protein,
                            sodium = nutrition.sodium
                        )
                    )
                },
                modifier = Modifier.width(160.dp),
                label = { Text("Sugar") },
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedFloatField(
                value = nutrition.fiber,
                onValueChange = {
                    onChangeNutritionalInfo(
                        NutritionInfo(
                            calories = nutrition.calories,
                            fats = nutrition.fats,
                            saturatedFats = nutrition.saturatedFats,
                            carbohydrates = nutrition.carbohydrates,
                            sugar = nutrition.sugar,
                            fiber = it,
                            protein = nutrition.protein,
                            sodium = nutrition.sodium
                        )
                    )
                },
                modifier = Modifier.width(100.dp),
                label = { Text("Fiber") },
            )
            OutlinedFloatField(
                value = nutrition.protein,
                onValueChange = {
                    onChangeNutritionalInfo(
                        NutritionInfo(
                            calories = nutrition.calories,
                            fats = nutrition.fats,
                            saturatedFats = nutrition.saturatedFats,
                            carbohydrates = nutrition.carbohydrates,
                            sugar = nutrition.sugar,
                            fiber = nutrition.fiber,
                            protein = it,
                            sodium = nutrition.sodium
                        )
                    )
                },
                modifier = Modifier.width(100.dp),
                label = { Text("Protein") },
            )
            OutlinedFloatField(
                value = nutrition.sodium,
                onValueChange = {
                    onChangeNutritionalInfo(
                        NutritionInfo(
                            calories = nutrition.calories,
                            fats = nutrition.fats,
                            saturatedFats = nutrition.saturatedFats,
                            carbohydrates = nutrition.carbohydrates,
                            sugar = nutrition.sugar,
                            fiber = nutrition.fiber,
                            protein = nutrition.protein,
                            sodium = it
                        )
                    )
                },
                modifier = Modifier.width(100.dp),
                label = { Text("Sodium") },
            )
        }
    }
}

@Preview
@Composable
fun RecipeItemAddScreenPreview() {
    PantryPlanTheme {
        Surface {
            RecipeItemAddScreen {
            }
        }
    }
}
