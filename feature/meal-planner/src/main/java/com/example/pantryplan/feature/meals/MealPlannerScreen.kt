@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.pantryplan.feature.meals

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.runtime.mutableStateOf
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
import coil3.compose.AsyncImage
import com.example.pantryplan.core.designsystem.component.ContentUnavailable
import com.example.pantryplan.core.designsystem.recipes.RecipeItemCard
import com.example.pantryplan.core.designsystem.theme.PantryPlanTheme
import com.example.pantryplan.core.models.NutritionInfo
import com.example.pantryplan.core.models.Recipe
import com.example.pantryplan.feature.meals.ui.MacrosCard
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.DayOfWeekNames
import kotlinx.datetime.toLocalDateTime
import java.util.UUID
import kotlin.time.Duration.Companion.days
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
        val commonModifier = modifier
            .padding(contentPadding)
            .padding(horizontal = dimensionResource(designSystemR.dimen.horizontal_margin))

        if (mealPlannerUiState.canUseMealPlanner.value) {
            MealPlannerScreenContent(
                mealPlannerUiState = mealPlannerUiState,
                onRecipeClick = onRecipeClick,
                onMacroCardClick = onMacroCardClick,
                modifier = commonModifier
            )
        } else {
            MealPlannerUnavailableView(
                modifier = commonModifier
            )
        }
    }
}

@Composable
private fun MealPlannerUnavailableView(
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxSize()
    ) {
        ContentUnavailable(
            painter = painterResource(designSystemR.drawable.feature_meal_planner_icon_outlined),
            title = "Meal Planner Unavailable",
            description = "You need at least 1 saved recipe to use the meal planner."
        )
    }
}

@Composable
private fun MealPlannerScreenContent(
    mealPlannerUiState: MealPlannerUiState,
    onRecipeClick: (UUID) -> Unit,
    onMacroCardClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement
            .spacedBy(4.dp),
        modifier = modifier
            .verticalScroll(rememberScrollState())
    ) {
        TodaysMeals(
            uiState = mealPlannerUiState,
            onRecipeClick = onRecipeClick
        )

        Macros(
            uiState = mealPlannerUiState,
            onMacroCardClick = onMacroCardClick
        )

        NextThreeDays(
            uiState = mealPlannerUiState,
            onRecipeClick = onRecipeClick
        )

        //Tips()
    }
}

@Composable
private fun Macros(
    uiState: MealPlannerUiState,
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
            item = uiState.dailyNutrition.value,
            onClick = onMacroCardClick
        )
    }
}

@Composable
private fun TodaysMeals(
    uiState: MealPlannerUiState,
    onRecipeClick: (UUID) -> Unit,
    modifier: Modifier = Modifier
) {
    // TODO: Create this list from the recommender system
    val meals = uiState.todaysMeals
    val mealsEaten = uiState.mealsEatenToday.intValue - 1

    val carouselState = rememberCarouselState(if (mealsEaten < 0) 0 else mealsEaten) { meals.size }

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
        AsyncImage(
            model = meal.imageUrl,
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
                },
            fallback = painterResource(designSystemR.drawable.default_recipe_thumbnail),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )

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
    uiState: MealPlannerUiState,
    onRecipeClick: (UUID) -> Unit,
    modifier: Modifier = Modifier
) {
    val format = LocalDate.Format {
        dayOfWeek(DayOfWeekNames.ENGLISH_FULL)
    }
    val twoDaysAwayDayName = Clock.System.now().plus(2.days)
        .toLocalDateTime(TimeZone.currentSystemDefault()).date.format(format)

    var selectedIndex by remember { mutableIntStateOf(0) }
    val options = listOf("Today", "Tomorrow", twoDaysAwayDayName)

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = "Next Three Days",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )

        SingleChoiceSegmentedButtonRow(
            modifier = Modifier
                .fillMaxWidth()
        ) {
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

        when (selectedIndex) {
            0 -> for (item in uiState.todaysMeals) {
                RecipeItemCard(
                    item = item,
                    onClick = {
                        onRecipeClick(item.id)
                    },
                    userAllergies = uiState.allergies
                )
            }

            1 -> for (item in uiState.tomorrowsMeals) {
                RecipeItemCard(
                    item = item,
                    onClick = {
                        onRecipeClick(item.id)
                    },
                    userAllergies = uiState.allergies
                )
            }

            2 -> for (item in uiState.twoDaysAwayMeals) {
                RecipeItemCard(
                    item = item,
                    onClick = {
                        onRecipeClick(item.id)
                    },
                    userAllergies = uiState.allergies
                )
            }

            else -> Text("Unable to retrieve upcoming meals.")
        }
    }
}

/*@Composable
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
}*/

@SuppressLint("UnrememberedMutableState")
@Preview(
    group = "Today's Meals",
    showBackground = true
)
@Composable
private fun TodaysMealsPreview() {
    PantryPlanTheme {
        TodaysMeals(
            uiState = MealPlannerUiState(canUseMealPlanner = mutableStateOf(true)),
            onRecipeClick = {}
        )
    }
}

@SuppressLint("UnrememberedMutableState")
@Preview(
    group = "Macros",
    showBackground = true
)
@Composable
private fun MacrosPreview() {
    PantryPlanTheme {
        Macros(
            uiState = MealPlannerUiState(
                dailyNutrition = mutableStateOf(
                    NutritionInfo(
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
            ),
            onMacroCardClick = {}
        )
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
            uiState = MealPlannerUiState(),
        )
    }
}

/*@Preview(
    group = "Tips",
    showBackground = true
)
@Composable
private fun TipsPreview() {
    PantryPlanTheme {
        Tips()
    }
}*/