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
import com.example.pantryplan.feature.meals.navigation.MealPlanner
import com.example.pantryplan.feature.pantry.navigation.PantryList
import com.example.pantryplan.feature.recipes.navigation.RecipesRoute
import kotlin.reflect.KClass
import com.example.pantryplan.feature.meals.R as mealsR
import com.example.pantryplan.feature.pantry.R as pantryR
import com.example.pantryplan.feature.recipes.R as recipesR

enum class TopLevelDestination (
    @StringRes val iconTextId: Int,
    @StringRes val titleTextId: Int,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val route: KClass<*>,
){
    PANTRY(
        pantryR.string.feature_pantry_title,
        pantryR.string.feature_pantry_title,
        Icons.Filled.Place,
        Icons.Outlined.Place,
        PantryList::class
    ),
    RECIPES(
        recipesR.string.feature_recipes_title,
        recipesR.string.feature_recipes_title,
        Icons.Rounded.Bookmark,
        Icons.Outlined.BookmarkBorder,
        RecipesRoute::class
    ),
    MEAL_PLANNER(
        mealsR.string.feature_meals_icon,
        mealsR.string.feature_meals_title,
        Icons.Filled.Notifications,
        Icons.Outlined.Notifications,
        MealPlanner::class
    ),
}