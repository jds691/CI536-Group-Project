package com.example.pantryplan.feature.meals

import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.mutableStateSetOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.snapshots.SnapshotStateSet
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pantryplan.core.data.access.repository.NutritionRepository
import com.example.pantryplan.core.data.access.repository.PlannedMealsRepository
import com.example.pantryplan.core.data.access.repository.RecipeRepository
import com.example.pantryplan.core.data.access.repository.UserPreferencesRepository
import com.example.pantryplan.core.datastore.UserPreferences
import com.example.pantryplan.core.models.Allergen
import com.example.pantryplan.core.models.NutritionInfo
import com.example.pantryplan.core.models.Recipe
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class MealPlannerViewModel @Inject constructor(
    userPreferencesRepository: UserPreferencesRepository,
    nutritionRepository: NutritionRepository,
    recipesRepository: RecipeRepository,
    plannedMealsRepository: PlannedMealsRepository
) : ViewModel() {
    val uiState: StateFlow<MealPlannerUiState> = mealPlannerUiState(
        userPreferencesRepository,
        nutritionRepository,
        recipesRepository,
        plannedMealsRepository
    )
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(1.seconds.inWholeMilliseconds),
            initialValue = MealPlannerUiState(),
        )
}

private fun mealPlannerUiState(
    userPreferencesRepository: UserPreferencesRepository,
    nutritionRepository: NutritionRepository,
    recipesRepository: RecipeRepository,
    plannedMealsRepository: PlannedMealsRepository
): Flow<MealPlannerUiState> {
    val preferencesFlow: Flow<UserPreferences> = userPreferencesRepository.preferences

    val nutritionFlow: Flow<NutritionInfo> = nutritionRepository.getNutritionForDate(
        Clock.System.now().toLocalDateTime(
            TimeZone.currentSystemDefault()
        ).date
    )

    val canUseMealPlannerFlow: Flow<Boolean> =
        recipesRepository.getAllRecipes().map { it.isNotEmpty() }

    val now = Clock.System.now()

    return combine(
        preferencesFlow,
        nutritionFlow,
        canUseMealPlannerFlow
    ) { preferences, nutrition, canUseMealPlanner ->

        val today = now.toLocalDateTime(TimeZone.currentSystemDefault()).date
        val tomorrow = now.plus(1.days).toLocalDateTime(TimeZone.currentSystemDefault()).date
        val twoDaysAway = now.plus(2.days).toLocalDateTime(TimeZone.currentSystemDefault()).date

        val todaysMealsRaw =
            plannedMealsRepository.getPlannedMealsForDate(today, preferences.expectedMealCount)
                .first()
        val tomorrowsMealsRaw =
            plannedMealsRepository.getPlannedMealsForDate(tomorrow, preferences.expectedMealCount)
                .first()
        val twoDaysAwayMealsRaw = plannedMealsRepository.getPlannedMealsForDate(
            twoDaysAway,
            preferences.expectedMealCount
        ).first()

        val recipeAllergySet: SnapshotStateSet<Allergen> = mutableStateSetOf()
        recipeAllergySet.addAll(preferences.allergies)

        val todaysMeals: SnapshotStateList<Recipe> = mutableStateListOf()
        todaysMeals.addAll(todaysMealsRaw)
        val tomorrowsMeals: SnapshotStateList<Recipe> = mutableStateListOf()
        tomorrowsMeals.addAll(tomorrowsMealsRaw)
        val twoDaysAwayMeals: SnapshotStateList<Recipe> = mutableStateListOf()
        twoDaysAwayMeals.addAll(twoDaysAwayMealsRaw)

        MealPlannerUiState(
            canUseMealPlanner = mutableStateOf(canUseMealPlanner),
            allergies = recipeAllergySet,
            dailyNutrition = mutableStateOf(nutrition),
            mealsEatenToday = mutableIntStateOf(0),
            expectedMealCount = mutableIntStateOf(preferences.expectedMealCount),

            todaysMeals = todaysMeals,
            tomorrowsMeals = tomorrowsMeals,
            twoDaysAwayMeals = twoDaysAwayMeals
        )
    }
}

data class MealPlannerUiState(
    val canUseMealPlanner: MutableState<Boolean> = mutableStateOf(false),
    val dailyNutrition: MutableState<NutritionInfo> = mutableStateOf(
        NutritionInfo(
            calories = 0,
            fats = 0f,
            saturatedFats = 0f,
            carbohydrates = 0f,
            sugar = 0f,
            fiber = 0f,
            protein = 0f,
            sodium = 0f
        )
    ),
    val allergies: SnapshotStateSet<Allergen> = mutableStateSetOf(),
    val mealsEatenToday: MutableIntState = mutableIntStateOf(0),
    val expectedMealCount: MutableIntState = mutableIntStateOf(3),
    val todaysMeals: SnapshotStateList<Recipe> = mutableStateListOf(),
    val tomorrowsMeals: SnapshotStateList<Recipe> = mutableStateListOf(),
    val twoDaysAwayMeals: SnapshotStateList<Recipe> = mutableStateListOf(),
)
