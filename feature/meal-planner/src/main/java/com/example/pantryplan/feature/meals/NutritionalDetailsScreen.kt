@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.pantryplan.feature.meals

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pantryplan.core.designsystem.R
import com.example.pantryplan.core.designsystem.theme.PantryPlanTheme
import com.example.pantryplan.core.models.NutritionInfo
import com.example.pantryplan.feature.meals.ui.MacrosCard
import java.text.DecimalFormat

@Composable
fun NutritionalDetailsScreen(
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
                .padding(innerPadding)
                .padding(horizontal = dimensionResource(R.dimen.horizontal_margin))
                .verticalScroll(rememberScrollState())
        ) {
            Macros()

            Nutrients()

            Tips()
        }
    }
}

@Composable
private fun Macros(
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
            item = NutritionInfo(
                calories = 500,
                fats = 13.35f,
                saturatedFats = 22f,
                carbohydrates = 65f,
                sugar = 12f,
                fiber = 34f,
                protein = 30f,
                sodium = 12f
            )
        )
    }
}

@Composable
private fun NutrientRow(
    name: String,
    amount: Float,
    target: Float,

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
            text = "${formatter.format(amount)}/${formatter.format(target)}${unit}",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun Nutrients(
    modifier: Modifier = Modifier
) {
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
            amount = 0f,
            target = 2000f,
            unit = "kcal"
        )

        HorizontalDivider()

        NutrientRow(
            name = "Fat",
            amount = 0f,
            target = 2000f,
        )

        HorizontalDivider()

        NutrientRow(
            name = "Saturated Fat",
            amount = 0f,
            target = 2000f,
        )

        HorizontalDivider()

        NutrientRow(
            name = "Carbohydrates",
            amount = 0f,
            target = 2000f,
        )

        HorizontalDivider()

        NutrientRow(
            name = "Sugar",
            amount = 0f,
            target = 2000f,
        )

        HorizontalDivider()

        NutrientRow(
            name = "Fiber",
            amount = 0f,
            target = 2000f,
        )

        HorizontalDivider()

        NutrientRow(
            name = "Protein",
            amount = 0f,
            target = 2000f,
        )

        HorizontalDivider()

        NutrientRow(
            name = "Sodium",
            amount = 0f,
            target = 2000f,
        )
    }
}

@Composable
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
}

@Preview
@Composable
private fun NutritionalDetailsScreenPreview() {
    PantryPlanTheme {
        NutritionalDetailsScreen(
            onBackClick = {}
        )
    }
}