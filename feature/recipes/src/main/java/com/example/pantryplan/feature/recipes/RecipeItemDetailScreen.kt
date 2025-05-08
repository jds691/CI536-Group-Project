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
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.AssistChip
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.material3.TextFieldLabelScope
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pantryplan.core.designsystem.theme.PantryPlanTheme
import com.example.pantryplan.core.models.Allergen
import com.example.pantryplan.core.models.NutritionInfo
import com.example.pantryplan.core.models.Recipe
import java.util.EnumSet
import java.util.UUID


internal fun cleanUpAllergenText(allergenName: String): String {
    var newAllergenName = allergenName.replace("_", " ")
    newAllergenName = newAllergenName.lowercase()
    newAllergenName =
        newAllergenName.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
    return newAllergenName
}


@Composable
fun RecipeItemDetailsScreen(
    item: Recipe,
    id: UUID,

    onBackClick: () -> Unit,
    onEditItem: (UUID) -> Unit
) {
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
                    IconButton(onClick = {}) {
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
                    text = item.title,
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
                    item.allergens.forEach { allergen ->
                        var curAllergen = allergen.toString()
                        curAllergen = cleanUpAllergenText(curAllergen)
                        AssistChip(
                            onClick = {},
                            label = { Text(curAllergen) }
                        )
                    }
                }
                Text(
                    text = item.description,
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
                        val recipeTime = DecimalFormat("#")
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
                                text = recipeTime.format(item.prepTime) + " Min",
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
                                text = recipeTime.format(item.cookTime) + " Min",
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
                                text = "" + item.nutrition.calories + " (Kcal)",
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
                                    .width(80.dp),
                                options = servingAmountList,
                            )
                            Text(
                                text = "Serving(s)",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
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
                        item.instructions.forEach { instruction ->
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
                        item.tags.forEach { tag ->
                            AssistChip(
                                onClick = {},
                                label = { Text(tag) }
                            )
                        }
                    }

                }
            }

        }
    }
}

@Composable
private fun OutlinedSelectField(
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
            modifier = Modifier,
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
            "Beef Burger",
            "Burger Buns",
            "American Cheese",
            "Lettuce",
            "Red Onion",
            "Bacon"
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
                item = recipe,
                id = UUID.randomUUID(),
                onBackClick = {},
                onEditItem = {})
        }
    }
}