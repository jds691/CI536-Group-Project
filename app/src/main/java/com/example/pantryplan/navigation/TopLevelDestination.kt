package com.example.pantryplan.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material.icons.rounded.Bookmark
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.pantryplan.R
import com.example.pantryplan.feature.meals.navigation.MealPlannerRoute
import com.example.pantryplan.feature.pantry.navigation.PantryRoute
import com.example.pantryplan.feature.recipes.navigation.RecipesRoute
import kotlin.reflect.KClass

enum class TopLevelDestination (
    @StringRes val label: Int,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val route: KClass<*>,
){
    PANTRY(
        R.string.pantry,
        Icons.Filled.Place,
        Icons.Outlined.Place,
        PantryRoute::class
    ),
    RECIPES(
        R.string.recipes,
        Icons.Rounded.Bookmark,
        Icons.Outlined.BookmarkBorder,
        RecipesRoute::class
    ),
    MEAL_PLANNER(
        R.string.mealplan,
        Icons.Filled.Notifications,
        Icons.Outlined.Notifications,
        MealPlannerRoute::class
    ),
}