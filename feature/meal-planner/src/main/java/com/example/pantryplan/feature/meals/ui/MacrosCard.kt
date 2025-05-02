package com.example.pantryplan.feature.meals.ui

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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.example.pantryplan.core.designsystem.theme.PantryPlanTheme
import com.example.pantryplan.core.models.NutritionInfo
import kotlin.math.roundToInt

@Composable
fun MacrosCard(
    item: NutritionInfo,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    //4 calories per gram of protein
    val proteinPercentage = (item.protein * 4 / item.calories * 1000).roundToInt() / 10f
    //4 calories per gram of carbs
    val carbohydratePercentage = (item.carbohydrates * 4 / item.calories * 1000).roundToInt() / 10f
    //9 calories per gram of fat
    val fatPercentage = (item.fats * 9 / item.calories * 1000).roundToInt() / 10f

    ElevatedCard(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
    ) {
        Column (
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterVertically),
        ) {
            Row {
                Text(
                    text = "Protein",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.weight(proteinPercentage),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    softWrap = false

                )
                Text(
                    text = "Carbs",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.weight(carbohydratePercentage),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    softWrap = false
                )
                Text(
                    text = "Fats",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.weight(fatPercentage),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    softWrap = false
                )
            }
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
                        text = "${proteinPercentage}%"
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
                        text = "${carbohydratePercentage}%"
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
                        text = "${fatPercentage}%"
                    )
                }
            }
            Row{
                Text(
                    text = "${item.protein}g",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.weight(proteinPercentage),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    softWrap = false
                )
                Text(
                    text = "${item.carbohydrates}g",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.weight(carbohydratePercentage),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    softWrap = false
                )
                Text(
                    text = "${item.fats}g",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.weight(fatPercentage),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    softWrap = false
                )
            }
        }
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
        )
    )
}