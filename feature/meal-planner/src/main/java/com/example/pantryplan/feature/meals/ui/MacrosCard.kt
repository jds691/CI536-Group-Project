package com.example.pantryplan.feature.meals.ui

import android.icu.text.DecimalFormat
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.example.pantryplan.core.designsystem.theme.PantryPlanTheme
import com.example.pantryplan.core.models.NutritionInfo
import com.example.pantryplan.feature.meals.R
import kotlin.math.roundToInt

@Composable
fun MacrosCard(
    item: NutritionInfo,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    ElevatedCard(
        enabled = onClick != null && item.calories > 0,
        onClick = onClick ?: {},
        modifier = modifier
            .fillMaxWidth()
    ) {
        Column (
            modifier = Modifier
                .background(MaterialTheme.colorScheme.tertiaryContainer)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterVertically),
        ) {
            if (item.calories == 0) {
                MacrosUnavailable()
            } else {
                NutritionalContents(item)
            }
        }
    }
}

@Composable
private fun MacrosUnavailable() {
    Text(
        text = stringResource(R.string.feature_meals_macros_error_title),
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.onTertiaryContainer
    )
    Box(
        modifier = Modifier
            .clip(MaterialTheme.shapes.small)
    ) {
        // This box wasn't allowed to clip itself for some reason
        Box(
            modifier = Modifier
                .height(28.dp)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.outlineVariant)
        )
    }
    Text(
        text = stringResource(R.string.feature_meals_macros_error_body),
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onTertiaryContainer
    )
}

@Composable
private fun NutritionalContents(
    item: NutritionInfo
) {
    //4 calories per gram of protein
    val proteinPercentage = (item.protein * 4 / item.calories * 1000).roundToInt() / 10f
    //4 calories per gram of carbs
    val carbohydratePercentage = (item.carbohydrates * 4 / item.calories * 1000).roundToInt() / 10f
    //9 calories per gram of fat
    val fatPercentage = (item.fats * 9 / item.calories * 1000).roundToInt() / 10f

    // One non-zero trailing decimal point
    val formatter = DecimalFormat("0.#")

    Row {
        Text(
            text = "Protein",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onTertiaryContainer,
            modifier = Modifier.weight(proteinPercentage),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            softWrap = false

        )
        Text(
            text = "Carbs",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onTertiaryContainer,
            modifier = Modifier.weight(carbohydratePercentage),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            softWrap = false
        )
        Text(
            text = "Fats",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onTertiaryContainer,
            modifier = Modifier.weight(fatPercentage),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            softWrap = false
        )
    }
    // TODO: Fix bar text colours if onClick = null
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(28.dp)
            .clip(MaterialTheme.shapes.small)
    ) {
        Box(

            contentAlignment = Alignment.Center,
            modifier = Modifier
                .weight(proteinPercentage)
                .fillMaxHeight()
                .background(Color(0xFFFFDAD6))
        ) {
            Text(
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onTertiaryContainer,
                text = "${formatter.format(proteinPercentage)}%"
            )
        }
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .weight(carbohydratePercentage)
                .fillMaxHeight()
                .background(Color(0xFFF9E287))
        ) {
            Text(
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onTertiaryContainer,
                text = "${formatter.format(carbohydratePercentage)}%"
            )
        }
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .weight(fatPercentage)
                .fillMaxHeight()
                .background(Color(0xFF9CF1ED))
        ) {
            Text(
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onTertiaryContainer,
                text = "${formatter.format(fatPercentage)}%"
            )
        }
    }
    Row {
        Text(
            text = "${formatter.format(item.protein)}g",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onTertiaryContainer,
            modifier = Modifier.weight(proteinPercentage),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            softWrap = false
        )
        Text(
            text = "${formatter.format(item.carbohydrates)}g",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onTertiaryContainer,
            modifier = Modifier.weight(carbohydratePercentage),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            softWrap = false
        )
        Text(
            text = "${formatter.format(item.fats)}g",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onTertiaryContainer,
            modifier = Modifier.weight(fatPercentage),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            softWrap = false
        )
    }
}

@Preview
@Composable
fun MacrosCardPreviews(@PreviewParameter(SampleNutritionInfoProvider::class) nutritionInfo: NutritionInfo) {
    PantryPlanTheme {
        MacrosCard(item = nutritionInfo)
    }
}

private class SampleNutritionInfoProvider : PreviewParameterProvider<NutritionInfo> {
    override val values: Sequence<NutritionInfo> = sequenceOf(
        NutritionInfo(
            calories = 500,
            fats = 13.35f,
            saturatedFats = 22f,
            carbohydrates = 65f,
            sugar = 12f,
            fiber = 34f,
            protein = 30f,
            sodium = 12f
        ),
        NutritionInfo(
            calories = 600,
            fats = 20f,
            saturatedFats = 10f,
            carbohydrates = 40f,
            sugar = 8f,
            fiber = 20f,
            protein = 50f,
            sodium = 15f
        ),
        NutritionInfo( // very low amounts of a single macro
            calories = 600,
            fats = 28f,
            saturatedFats = 10f,
            carbohydrates = 40f,
            sugar = 8f,
            fiber = 75f,
            protein = 12f,
            sodium = 15f
        ),
        NutritionInfo( // very low amounts of a single macro
            calories = 0,
            fats = 0f,
            saturatedFats = 0f,
            carbohydrates = 0f,
            sugar = 0f,
            fiber = 0f,
            protein = 0f,
            sodium = 0f
        )
    )
}