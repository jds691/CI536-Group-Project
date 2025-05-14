@file:OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)

package com.example.pantryplan.feature.recipes.ui

import android.icu.text.DecimalFormat
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.pantryplan.core.designsystem.theme.PantryPlanTheme
import com.example.pantryplan.core.models.Ingredient
import com.example.pantryplan.core.models.Measurement
import com.example.pantryplan.core.models.PantryItem
import com.example.pantryplan.core.models.PantryItemState
import com.example.pantryplan.feature.recipes.R
import kotlinx.datetime.Clock
import java.util.UUID
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days

@Composable
fun RecipeIngredientCard(
    modifier: Modifier = Modifier,
    ingredientData: Ingredient,

    onClick: () -> Unit = {},
) {

    val measurementSignifier: String = when (ingredientData.measurement) {
        Measurement.GRAMS -> "g"
        Measurement.KILOGRAMS -> "kg"
        Measurement.OTHER -> ""
    }

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = CardDefaults.elevatedShape
            ),
        onClick = onClick,
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.Start),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = ingredientData.linkedPantryItem?.imageUrl,
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(1.0f),
                fallback = painterResource(R.drawable.default_recipe_thumbnail),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 9.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.Top),
                horizontalAlignment = Alignment.Start,
            ) {

                var actualUseAmount = ingredientData.amount
                if (ingredientData.measurement == Measurement.KILOGRAMS) {
                    actualUseAmount *= 1000
                }

                val progressDecrease =
                    actualUseAmount / ingredientData.linkedPantryItem!!.quantity.toFloat()
                val progressAmount = 1.0f - progressDecrease
                var progressColor = Color.Green

                val displayAmount = DecimalFormat("#.##")

                var displayPantryAmount = ingredientData.linkedPantryItem!!.quantity.toFloat()
                var pantryMeasurementSignifier = "g"

                if (ingredientData.linkedPantryItem!!.quantity.toFloat() > 999) {
                    displayPantryAmount =
                        ingredientData.linkedPantryItem!!.quantity.toFloat() / 1000
                    pantryMeasurementSignifier = "kg"
                }

                Text(
                    text = ingredientData.name,
                    style = MaterialTheme.typography.titleMedium
                )
                if (progressAmount >= 0f) {
                    Text(
                        text = "In pantry: " + displayAmount.format(displayPantryAmount) + pantryMeasurementSignifier + " - Uses: " + displayAmount.format(
                            ingredientData.amount
                        ) + measurementSignifier,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                } else {
                    Text(
                        text = "! Not enough | In pantry: " + displayAmount.format(
                            displayPantryAmount
                        ) + pantryMeasurementSignifier + " - Uses: " + displayAmount.format(
                            ingredientData.amount
                        ) + measurementSignifier,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error
                    )
                    progressColor = MaterialTheme.colorScheme.error
                }

                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth(),
                    progress = { progressAmount },
                    color = progressColor,
                    trackColor = progressColor.copy(0.2f)
                )
            }
        }
    }
}

@Preview
@Composable
fun RecipeIngredientCardPreview() {
    PantryPlanTheme {
        RecipeIngredientCard(
            modifier = Modifier,
            Ingredient(
                name = "Beef Burgers",
                amount = 200f,
                measurement = Measurement.GRAMS,
                linkedPantryItem = PantryItem(
                    id = UUID.randomUUID(),
                    name = "Beef Burgers",
                    quantity = 1500f,
                    expiryDate = Clock.System.now() + 7.days,
                    expiresAfter = Duration.ZERO,
                    inStateSince = Clock.System.now(),
                    state = PantryItemState.SEALED,
                    imageUrl = null,
                    barcode = null,
                    measurement = Measurement.GRAMS
                )
            )
        )
    }
}
