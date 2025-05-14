@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.pantryplan.feature.recipes

import android.icu.text.DecimalFormat
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.ChipColors
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.pantryplan.core.designsystem.theme.PantryPlanTheme
import com.example.pantryplan.core.models.Allergen
import com.example.pantryplan.core.models.Ingredient
import com.example.pantryplan.core.models.Measurement
import com.example.pantryplan.core.models.NutritionInfo
import com.example.pantryplan.core.models.PantryItem
import com.example.pantryplan.core.models.PantryItemState
import com.example.pantryplan.core.models.Recipe
import com.example.pantryplan.feature.recipes.ui.IngredientCard
import com.example.pantryplan.feature.recipes.ui.RecipeIngredientCard
import kotlinx.datetime.Clock
import java.util.EnumSet
import java.util.UUID
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days


internal fun cleanUpAllergenText(allergenName: String): String {
    var newAllergenName = allergenName.replace("_", " ")
    newAllergenName = newAllergenName.lowercase()
    newAllergenName =
        newAllergenName.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
    return newAllergenName
}


@Composable
fun RecipeItemDetailsScreen(
    viewModel: RecipeDetailViewModel = hiltViewModel(),
    id: UUID,

    onBackClick: () -> Unit,
    onEditItem: (UUID) -> Unit

) {
    val recipeDetailUiState by viewModel.uiState.collectAsStateWithLifecycle()
    val servingAmount by viewModel.servingAmount.collectAsStateWithLifecycle()

    RecipeItemDetailsScreen(
        recipeDetailUiState = recipeDetailUiState,
        id = id,

        onBackClick = onBackClick,
        onEditItem = onEditItem,
        servingAmount = servingAmount,
        onServingChange = viewModel::changeServingAmount
    )

}

@Composable
internal fun RecipeItemDetailsScreen(
    recipeDetailUiState: RecipeDetailsUiState,
    id: UUID,

    onBackClick: () -> Unit,
    onEditItem: (UUID) -> Unit,
    servingAmount: Int,
    onServingChange: (Int) -> Unit
) {
    val recipe = recipeDetailUiState.recipe
    var showDeleteDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Recipe Item Details")
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "")
                    }
                },
                actions = {
                    IconButton(onClick = { showDeleteDialog = true }) {
                        Icon(Icons.Outlined.Delete, "")
                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar(
                actions = {

                },
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = {
                            onEditItem(id)
                        },
                        containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                        elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
                    ) {
                        Icon(Icons.Outlined.Edit, "")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .consumeWindowInsets(innerPadding)
                .padding(innerPadding)
        ) {
            Image(
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth(),
                painter = painterResource(R.drawable.cheeseburger),
                contentDescription = null,
                contentScale = ContentScale.FillBounds

            )
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Top),
                modifier = Modifier
                    .padding(16.dp, 8.dp, 16.dp, 16.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = recipe.title,
                    style = MaterialTheme.typography.titleMedium
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.Start),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Allergens:",
                        style = MaterialTheme.typography.labelLarge
                    )
                    val allergenSet = recipeDetailUiState.allergies
                    var chipEnabled = true
                    recipe.allergens.forEach { allergen ->

                        if (allergenSet.contains(allergen)) {
                            chipEnabled = false
                        } else {
                            chipEnabled = true
                        }

                        var curAllergen = allergen.toString()
                        curAllergen = cleanUpAllergenText(curAllergen)
                        AssistChip(
                            onClick = {},
                            label = { Text(curAllergen) },
                            enabled = chipEnabled,
                            colors = ChipColors(
                                containerColor = Color.Unspecified,
                                labelColor = Color.Unspecified,
                                leadingIconContentColor = Color.Unspecified,
                                trailingIconContentColor = Color.Unspecified,
                                disabledContainerColor = MaterialTheme.colorScheme.errorContainer,
                                disabledLabelColor = MaterialTheme.colorScheme.error,
                                disabledLeadingIconContentColor = Color.Unspecified,
                                disabledTrailingIconContentColor = Color.Unspecified
                            )
                        )
                    }
                }
                Text(
                    text = recipe.description,
                    style = MaterialTheme.typography.bodyMedium
                )
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.Top),
                    horizontalAlignment = Alignment.Start,
                ) {

                    HorizontalDivider(
                        modifier = Modifier
                            .padding(0.dp, 4.dp, 0.dp, 4.dp)
                    )

                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp, 0.dp, 32.dp, 0.dp)
                    ) {
                        val recipeTime = DecimalFormat("#.##")
                        val timeColor = MaterialTheme.colorScheme.onSurfaceVariant
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Prep",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = recipeTime.format(recipe.prepTime) + " Min",
                                style = MaterialTheme.typography.bodyMedium,
                                color = timeColor
                            )
                        }
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Cook",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = recipeTime.format(recipe.cookTime) + " Min",
                                style = MaterialTheme.typography.bodyMedium,
                                color = timeColor
                            )
                        }
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Calories",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = "" + recipe.nutrition.calories + " (Kcal)",
                                style = MaterialTheme.typography.bodyMedium,
                                color = timeColor
                            )
                        }
                    }

                    HorizontalDivider(
                        modifier = Modifier
                            .padding(0.dp, 4.dp, 0.dp, 4.dp)
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Ingredients",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.Start),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val servingAmountList = List(10) { "${it + 1}" }
                            OutlinedSelectField(
                                modifier = Modifier
                                    .width(125.dp),
                                options = servingAmountList,
                                initialSelectedIndex = 0,
                                onUpdate = {
                                    onServingChange(it.toInt())
                                }

                            )
                            Text(
                                text = "Serving(s)",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Top),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        recipe.ingredients.forEach { ingredient ->
                            if (ingredient.linkedPantryItem == null) {
                                IngredientCard(
                                    modifier = Modifier,
                                    Ingredient(
                                        name = ingredient.name,
                                        amount = (ingredient.amount * servingAmount),
                                        measurement = ingredient.measurement,
                                        linkedPantryItem = ingredient.linkedPantryItem
                                    ),
                                )
                            } else {
                                RecipeIngredientCard(
                                    ingredientData = Ingredient(
                                        name = ingredient.name,
                                        amount = (ingredient.amount * servingAmount),
                                        measurement = ingredient.measurement,
                                        linkedPantryItem = ingredient.linkedPantryItem
                                    )
                                )
                            }
                        }
                    }

                    HorizontalDivider(
                        modifier = Modifier
                            .padding(0.dp, 4.dp, 0.dp, 4.dp)
                    )

                    Text(
                        text = "Instructions",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Column(
                        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Top),
                        horizontalAlignment = Alignment.Start
                    ) {
                        var stepNum = 1
                        recipe.instructions.forEach { instruction ->
                            Text(
                                text = "Step $stepNum - $instruction",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            stepNum++
                        }
                    }

                    HorizontalDivider(
                        modifier = Modifier
                            .padding(0.dp, 4.dp, 0.dp, 4.dp)
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.Start),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Tags:",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                        recipe.tags.forEach { tag ->
                            AssistChip(
                                onClick = {},
                                label = { Text(tag) }
                            )
                        }
                    }

                    if (showDeleteDialog) {
                        AlertDialog(
                            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                            onDismissRequest = { showDeleteDialog = false },
                            title = {
                                Text(
                                    text = "Delete '${recipe.title}'?",
                                    color = MaterialTheme.colorScheme.onSurface,
                                    style = MaterialTheme.typography.headlineSmall
                                )
                            },
                            text = {
                                Text(
                                    text = "This recipe cannot be restored. Are you sure you want to delete it?",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            },
                            confirmButton = {
                                TextButton(
                                    onClick = {
                                        //TODO: add functionality to delete recipe
                                        showDeleteDialog = false

                                        onBackClick() // pops back to pantry screen when item is deleted
                                    }
                                ) {
                                    Text(
                                        text = "Delete",
                                        color = MaterialTheme.colorScheme.primary,
                                        style = MaterialTheme.typography.labelLarge
                                    )
                                }
                            },
                            dismissButton = {
                                TextButton(onClick = { showDeleteDialog = false }) {
                                    Text(
                                        text = "Cancel",
                                        color = MaterialTheme.colorScheme.primary,
                                        style = MaterialTheme.typography.labelLarge
                                    )

                                }
                            }
                        )
                    }

                }
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
fun RecipesDetailPreview() {
    val recipe = Recipe(
        id = UUID.randomUUID(),
        title = "Cheeseburger",
        description = "Burger packed with juicy beef, melted cheese and extra vegetables to add that final flavour.",
        tags = listOf("Dinner", "High Protein"),
        allergens = EnumSet.of(Allergen.MILK, Allergen.GLUTEN, Allergen.SESAME),
        imageUrl = null,
        instructions = listOf("Cook Burger", "Eat burger"),
        ingredients = listOf(
            Ingredient(
                name = "American Cheese",
                amount = 200f,
                measurement = Measurement.GRAMS,
                linkedPantryItem = PantryItem(
                    id = UUID.randomUUID(),
                    name = "Beef Burgers",
                    quantity = 600,
                    expiryDate = Clock.System.now() + 7.days,
                    expiresAfter = Duration.ZERO,
                    inStateSince = Clock.System.now(),
                    state = PantryItemState.SEALED,
                    imageUrl = null,
                    barcode = null,
                )
            ),
            Ingredient(
                name = "American Cheese",
                amount = 200f,
                measurement = Measurement.GRAMS,
                linkedPantryItem = null
            )
        ),
        prepTime = 10f,
        cookTime = 15f,
        nutrition = NutritionInfo(
            calories = 100,
            fats = 100f,
            saturatedFats = 100f,
            carbohydrates = 100f,
            sugar = 100f,
            fiber = 100f,
            protein = 100f,
            sodium = 100f
        )
    )
    PantryPlanTheme {
        Surface {
            RecipeItemDetailsScreen(
                //recipeDetailUiState = RecipePreferencesUiState(),
                id = UUID.randomUUID(),
                onBackClick = {},
                onEditItem = {})
        }
    }
}