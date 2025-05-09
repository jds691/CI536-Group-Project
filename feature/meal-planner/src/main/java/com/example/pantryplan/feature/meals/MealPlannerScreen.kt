@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.pantryplan.feature.meals

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.carousel.CarouselDefaults
import androidx.compose.material3.carousel.HorizontalUncontainedCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.pantryplan.core.designsystem.recipes.RecipeItemCard
import com.example.pantryplan.core.designsystem.theme.PantryPlanTheme
import com.example.pantryplan.core.models.Allergen
import com.example.pantryplan.core.models.NutritionInfo
import com.example.pantryplan.core.models.Recipe
import com.example.pantryplan.feature.meals.ui.MacrosCard
import java.util.EnumSet
import java.util.UUID
import com.example.pantryplan.core.designsystem.R as designSystemR

@Composable
fun MealPlannerScreen(
    viewModel: MealPlannerViewModel = hiltViewModel(),
    onRecipeClick: (UUID) -> Unit,
    onMacroCardClick: () -> Unit,

    modifier: Modifier = Modifier
) {
    val mealPlannerUiState: MealPlannerUiState by viewModel.uiState.collectAsStateWithLifecycle()
    MealPlannerScreen(
        mealPlannerUiState = mealPlannerUiState,
        onRecipeClick = onRecipeClick,
        onMacroCardClick = onMacroCardClick,

        modifier = modifier

    )
}


@Composable
fun MealPlannerScreen(
    mealPlannerUiState: MealPlannerUiState,
    onRecipeClick: (UUID) -> Unit,
    onMacroCardClick: () -> Unit,

    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = Modifier
            .systemBarsPadding()
            .padding(
                top = dimensionResource(designSystemR.dimen.top_app_bar_height),
                bottom = dimensionResource(designSystemR.dimen.bottom_app_bar_height)
            )
    ) { contentPadding ->
        Column(
            verticalArrangement = Arrangement
                .spacedBy(4.dp),
            modifier = modifier
                .padding(contentPadding)
                .padding(horizontal = dimensionResource(designSystemR.dimen.horizontal_margin))
                .verticalScroll(rememberScrollState())
        ) {
            TodaysMeals(
                onRecipeClick = onRecipeClick
            )

            Macros(
                onMacroCardClick = onMacroCardClick
            )

            NextThreeDays(
                mealPlannerUiState = mealPlannerUiState,
                onRecipeClick = onRecipeClick
            )

            Tips()
        }
    }
}

@Composable
private fun Macros(
    onMacroCardClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
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
            ),
            onClick = onMacroCardClick
        )
    }
}

@Composable
private fun TodaysMeals(
    onRecipeClick: (UUID) -> Unit,
    modifier: Modifier = Modifier
) {
    val previewMeal = Recipe(
        id = UUID.randomUUID(),
        title = "Egg wif Hat",
        description = "The immortal one.",
        tags = listOf(),
        allergens = EnumSet.of(Allergen.EGGS),
        imageUrl = null,
        instructions = listOf(),
        ingredients = listOf(),
        prepTime = 0.0f,
        cookTime = 0.0f,
        nutrition = NutritionInfo(
            calories = 1_000_000,
            fats = 0.0f,
            saturatedFats = 0.0f,
            carbohydrates = 0.0f,
            sugar = 0.0f,
            fiber = 0.0f,
            protein = 0.0f,
            sodium = 0.0f
        )
    )

    // TODO: Create this list from the recommender system
    val meals = listOf(
        previewMeal,
        previewMeal,
        previewMeal
    )

    val carouselState = rememberCarouselState { meals.size }

    Column(
        modifier = modifier
    ) {
        Text(
            text = "Today's Meals",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )

        HorizontalUncontainedCarousel(
            state = carouselState,
            modifier = Modifier
                .height(221.dp),
            itemWidth = 316.dp,
            itemSpacing = 8.dp,
            flingBehavior = CarouselDefaults.singleAdvanceFlingBehavior(carouselState)
        ) { mealIndex ->
            val meal = meals[mealIndex]

            CarouselMealCard(
                meal = meal,
                modifier = Modifier
                    .maskClip(MaterialTheme.shapes.extraLarge),
                onClick = {
                    onRecipeClick(meal.id)
                }
            )
        }
    }
}

@Composable
private fun CarouselMealCard(
    meal: Recipe,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .clickable(onClick = onClick)
    ){
        if (meal.imageUrl != null) {
            // TODO: Show image async loaded (afaik not supported by Compose natively)
        } else {
            Image(
                painter = painterResource(R.drawable.default_recipe_thumbnail),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .width(316.dp)
                    .height(205.dp)

                    // Slight gradient overlay as shown in Figma
                    .drawWithContent {
                        drawContent()

                        drawRect(
                            Brush.linearGradient(
                                listOf(Color.Black.copy(alpha = 0.5f), Color.Transparent),

                                // Rotates the gradient by 90 degrees
                                start = Offset(0f, Float.POSITIVE_INFINITY),
                                end = Offset(0f, 0f)
                            )
                        )
                    }
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = meal.title,
                style = MaterialTheme.typography.titleMedium,
                color = Color.White
            )
            Text(
                text = "${meal.nutrition.calories} kcal",
                style = MaterialTheme.typography.labelSmall,
                color = Color.White
            )
        }
    }
}

@Composable
private fun NextThreeDays(
    mealPlannerUiState: MealPlannerUiState,
    onRecipeClick: (UUID) -> Unit,
    modifier: Modifier = Modifier
) {
    val previewMeal = Recipe(
        id = UUID.randomUUID(),
        title = "Egg wif Hat",
        description = "The immortal one.",
        tags = listOf(),
        allergens = EnumSet.of(Allergen.EGGS),
        imageUrl = null,
        instructions = listOf(),
        ingredients = listOf(),
        prepTime = 0.0f,
        cookTime = 0.0f,
        nutrition = NutritionInfo(
            calories = 1_000_000,
            fats = 0.0f,
            saturatedFats = 0.0f,
            carbohydrates = 0.0f,
            sugar = 0.0f,
            fiber = 0.0f,
            protein = 0.0f,
            sodium = 0.0f
        )
    )

    // TODO: Create this list from the recommender system
    val meals = listOf(
        previewMeal,
        previewMeal,
        previewMeal
    )

    var selectedIndex by remember { mutableIntStateOf(0) }
    val options = listOf("Today", "Tomorrow", "Wednesday")

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = "Next Three Days",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )

        SingleChoiceSegmentedButtonRow {
            options.forEachIndexed { index, label ->
                SegmentedButton(
                    shape = SegmentedButtonDefaults.itemShape(
                        index = index,
                        count = options.size
                    ),
                    onClick = { selectedIndex = index },
                    selected = index == selectedIndex,
                    label = { Text(text = label) }
                )
            }
        }

        /*when(selectedIndex) {
            0 -> Text("Unable to retrieve upcoming meals for today.")
            1 -> Text("Unable to retrieve upcoming meals for tomorrow.")
            2 -> Text("Unable to retrieve upcoming meals for Wednesday.")

            else -> Text("Unable to retrieve upcoming meals.")
        }*/
        for (meal: Recipe in meals) {
            RecipeItemCard(
                item = meal,
                onClick = {
                    onRecipeClick(meal.id)
                },
                userAllergies = mealPlannerUiState.allergies
            )
        }
    }
}

@Composable
private fun Tips(modifier: Modifier = Modifier) {
    Column(
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

@Preview(
    group = "Today's Meals",
    showBackground = true
)
@Composable
private fun TodaysMealsPreview() {
    PantryPlanTheme {
        TodaysMeals(onRecipeClick = {})
    }
}

@Preview(
    group = "Macros",
    showBackground = true
)
@Composable
private fun MacrosPreview() {
    PantryPlanTheme {
        Macros(onMacroCardClick = {})
    }
}

@Preview(
    group = "Next Three Days",
    showBackground = true
)
@Composable
private fun NextThreeDaysPreview() {
    PantryPlanTheme {
        NextThreeDays(
            onRecipeClick = {},
            mealPlannerUiState = MealPlannerUiState(),
        )
    }
}

@Preview(
    group = "Tips",
    showBackground = true
)
@Composable
private fun TipsPreview() {
    PantryPlanTheme {
        Tips()
    }
}