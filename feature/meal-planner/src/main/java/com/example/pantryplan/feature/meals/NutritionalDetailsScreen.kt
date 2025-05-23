@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.pantryplan.feature.meals

import android.icu.text.DecimalFormat
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.pantryplan.core.designsystem.R
import com.example.pantryplan.core.designsystem.theme.PantryPlanTheme
import com.example.pantryplan.core.models.NutritionInfo
import com.example.pantryplan.feature.meals.ui.MacrosCard

@Composable
fun NutritionalDetailsScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: NutritionalDetailsViewModel = hiltViewModel()
) {
    val uiState: NutritionalDetailsUiState by viewModel.uiState.collectAsStateWithLifecycle()

    NutritionalDetailsScreen(
        uiState = uiState,
        onBackClick = onBackClick,
        modifier = modifier
    )
}

@Composable
fun NutritionalDetailsScreen(
    uiState: NutritionalDetailsUiState,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            MediumTopAppBar(
                title = { Text("Your Nutrition") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back to Meal Planner")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            verticalArrangement = Arrangement
                .spacedBy(8.dp),
            modifier = modifier
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding)
                .padding(horizontal = dimensionResource(R.dimen.horizontal_margin))
        ) {
            Macros(
                uiState = uiState
            )

            Nutrients(
                uiState = uiState
            )

            //Tips()
        }
    }
}

@Composable
private fun Macros(
    uiState: NutritionalDetailsUiState,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement
            .spacedBy(4.dp),

        modifier = modifier
    ) {
        Text(
            text = "Macros",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )

        MacrosCard(
            item = uiState.nutrition
        )

        /*Text(
            // TODO: Replace styled bold text with correct values
            buildAnnotatedString {
                append("Your goal is to ")

                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("gain muscle")
                }

                append(". This means:")
            }
        )

        BulletedNutrientGoal(
            nutrient = "protein",
            range = "25-30%"
        )

        BulletedNutrientGoal(
            nutrient = "carbs",
            range = "55-60%"
        )

        BulletedNutrientGoal(
            nutrient = "fats",
            range = "15-20%"
        )

        Text(
            buildAnnotatedString {
                append("\n")
                append("You are currently ")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("out of range")
                }
                append(".")
            }
        )*/
    }
}

@Composable
private fun BulletedNutrientGoal(
    nutrient: String,
    range: String
) {
    Text(
        buildAnnotatedString {
            append("\u2022")
            append("\t\t")
            append("Your $nutrient should be between ")
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                append(range)
            }
        }
    )
}

@Composable
private fun NutrientRow(
    name: String,
    amount: Float,
    //target: Float,

    modifier: Modifier = Modifier,
    unit: String = "g"
) {
    // One non-zero trailing decimal point
    val formatter = DecimalFormat("0.#")

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 40.dp)
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.labelMedium
        )

        Text(
            text = "${formatter.format(amount)}${unit}",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun Nutrients(
    uiState: NutritionalDetailsUiState,
    modifier: Modifier = Modifier
) {
    val nutrition = uiState.nutrition

    Column(
        verticalArrangement = Arrangement
            .spacedBy(4.dp),

        modifier = modifier
    ) {
        Text(
            text = "Nutrients",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )

        NutrientRow(
            name = "Calories",
            amount = nutrition.calories.toFloat(),
            unit = " kcal"
        )

        HorizontalDivider()

        NutrientRow(
            name = "Fat",
            amount = nutrition.fats,
        )

        HorizontalDivider()

        NutrientRow(
            name = "Saturated Fat",
            amount = nutrition.saturatedFats,
        )

        HorizontalDivider()

        NutrientRow(
            name = "Carbohydrates",
            amount = nutrition.carbohydrates,
        )

        HorizontalDivider()

        NutrientRow(
            name = "Sugar",
            amount = nutrition.sugar,
        )

        HorizontalDivider()

        NutrientRow(
            name = "Fiber",
            amount = nutrition.fiber,
        )

        HorizontalDivider()

        NutrientRow(
            name = "Protein",
            amount = nutrition.protein,
        )

        HorizontalDivider()

        NutrientRow(
            name = "Sodium",
            amount = nutrition.sodium,
        )
    }
}

/*@Composable
private fun Tips(
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement
            .spacedBy(4.dp),

        modifier = modifier
    ) {
        Text(
            text = "Tips",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Text("No tips currently available.")

        // TODO: Show a list of cards that can navigate to various tips
    }
}*/

@Preview
@Composable
private fun NutritionalDetailsScreenPreview() {
    PantryPlanTheme {
        NutritionalDetailsScreen(
            uiState = NutritionalDetailsUiState(
                nutrition = NutritionInfo(
                    calories = 500,
                    fats = 13.35f,
                    saturatedFats = 22f,
                    carbohydrates = 65f,
                    sugar = 12f,
                    fiber = 34f,
                    protein = 30f,
                    sodium = 12f
                )
            ),
            onBackClick = {}
        )
    }
}