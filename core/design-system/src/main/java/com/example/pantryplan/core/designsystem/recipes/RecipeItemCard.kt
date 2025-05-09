@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.pantryplan.core.designsystem.recipes

import android.annotation.SuppressLint
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pantryplan.core.designsystem.R
import com.example.pantryplan.core.designsystem.component.DeleteAlertDialog
import com.example.pantryplan.core.designsystem.theme.PantryPlanTheme
import com.example.pantryplan.core.models.Allergen
import com.example.pantryplan.core.models.NutritionInfo
import com.example.pantryplan.core.models.Recipe
import java.util.EnumSet
import java.util.UUID

internal fun cleanUpAllergenText(allergenName: String): String {
    var newAllergenName = allergenName.replace("_", " ")
    newAllergenName = newAllergenName.lowercase()
    newAllergenName = newAllergenName.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
    return newAllergenName
}

@Composable
fun RecipeItemCard(
    item: Recipe,
    userAllergies: Set<Allergen>,
    modifier: Modifier = Modifier,

    onClick: () -> Unit = {},
    onLongClick: () -> Unit = {},
    onDelete: (() -> Unit)? = null,
) {
    val allergenText = buildAnnotatedString {
        var allergenLimitCounter = 0
        var allergenOverflowCounter = 0

        append("Allergens: ")

        for (allergen in item.allergens) {
            if (allergenLimitCounter >= 3) { allergenOverflowCounter++ }
            else {
                var curAllergen = allergen.toString()

                if (userAllergies.contains(allergen)) {
                    pushStyle(SpanStyle(color = MaterialTheme.colorScheme.error))
                    curAllergen = cleanUpAllergenText(curAllergen)
                    if (allergen == item.allergens.last()) { append(curAllergen) }
                    else { append("$curAllergen, ") }
                    pop()
                } else {
                    curAllergen = cleanUpAllergenText(curAllergen)
                    if (allergen == item.allergens.last()) { append(curAllergen) }
                    else { append("$curAllergen, ") }
                }
                allergenLimitCounter++
            }

        }
        if (allergenOverflowCounter > 0) { append(" + $allergenOverflowCounter")}
    }

    val showDeleteAlert = remember { mutableStateOf(false) }
    val isResetting = remember { mutableStateOf(false) }

    val dismissState = rememberSwipeToDismissBoxState()
    SwipeToDismissBox(
        state = dismissState,
        enableDismissFromStartToEnd = false,
        enableDismissFromEndToStart = onDelete != null,
        backgroundContent = {
            val backgroundColour by animateColorAsState(
                when (dismissState.targetValue) {
                    SwipeToDismissBoxValue.Settled -> MaterialTheme.colorScheme.surface
                    SwipeToDismissBoxValue.EndToStart -> Color.Red
                    else -> Color.Unspecified
                }
            )

            val iconColour by animateColorAsState(
                when (dismissState.targetValue) {
                    SwipeToDismissBoxValue.Settled -> MaterialTheme.colorScheme.surface
                    SwipeToDismissBoxValue.EndToStart -> Color.White
                    else -> Color.Unspecified
                }
            )

            Box(
                Modifier
                    .fillMaxSize()
                    .background(
                        color = backgroundColour,
                        shape = CardDefaults.elevatedShape
                    )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = iconColour
                    )
                }
            }
        },
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
    ) {
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .combinedClickable(
                    onClick = onClick,
                    onLongClick = onLongClick
                )
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.Start),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (item.imageUrl != null) {
                    //TODO: Place recipe item image in card when there is an active image passed in
                } else {
                    Image(
                        modifier = Modifier
                            .fillMaxHeight()
                            .aspectRatio(1.0f),
                        painter = painterResource(R.drawable.cheeseburger),
                        contentDescription = null,
                        contentScale = ContentScale.Crop
                    )
                }
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.Top),
                    horizontalAlignment = Alignment.Start,
                ) {


                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = allergenText,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
        }
    }

    if (onDelete != null && showDeleteAlert.value) {
        DeleteAlertDialog(
            itemName = item.title,
            showAlert = showDeleteAlert,
            onDelete = onDelete
        )
    }

    LaunchedEffect(isResetting.value) {
        if (isResetting.value) {
            // Reset is supposed to be called in a LaunchedEffect
            dismissState.reset()
            isResetting.value = false
        }
    }

    // If card is not at the start, but the card is fully in one direction e.g. SwipeToDismissBoxValue.EndToStart
    // Effectively checks if the card needs to be reset to the center
    val swipeNeedsReset =
        dismissState.currentValue != SwipeToDismissBoxValue.Settled && dismissState.progress == 1.0f

    if (!isResetting.value && !showDeleteAlert.value && swipeNeedsReset) {
        isResetting.value = true
    }

    // If card has been fully slided to the end
    val slideFinished =
        dismissState.targetValue == SwipeToDismissBoxValue.EndToStart && dismissState.progress == 1.0f

    if (!isResetting.value && !showDeleteAlert.value && slideFinished) {
        showDeleteAlert.value = true
    }
}

@SuppressLint("UnrememberedMutableState")
@Preview
@Composable
internal fun RecipeItemCardPreviews() {
    val recipe = Recipe(
        id = UUID.randomUUID(),
        title = "Cheeseburger",
        description = "Burger packed with juicy beef, melted cheese and extra vegetables to add that final flavour.",
        tags = listOf("Dinner", "High Protein"),
        allergens = EnumSet.of(Allergen.MILK, Allergen.GLUTEN, Allergen.SESAME),
        imageUrl = null,
        instructions = listOf("1. Cook Burger", "2. Eat burger"),
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
        RecipeItemCard(
            userAllergies = emptySet(),
            item = recipe
        )
    }
}
