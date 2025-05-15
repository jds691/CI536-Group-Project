package com.example.pantryplan.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.pantryplan.feature.meals.navigation.MealPlanner
import com.example.pantryplan.feature.pantry.navigation.PantryList
import com.example.pantryplan.feature.recipes.navigation.RecipesList
import kotlin.reflect.KClass
import com.example.pantryplan.core.designsystem.R as designSystemR
import com.example.pantryplan.feature.meals.R as mealsR
import com.example.pantryplan.feature.pantry.R as pantryR
import com.example.pantryplan.feature.recipes.R as recipesR

enum class TopLevelDestination (
    @StringRes val iconTextId: Int,
    @StringRes val titleTextId: Int,
    @DrawableRes val selectedIconId: Int,
    @DrawableRes val unselectedIconId: Int,
    val route: KClass<*>,
){
    PANTRY(
        pantryR.string.feature_pantry_title,
        pantryR.string.feature_pantry_title,
        designSystemR.drawable.feature_pantry_icon_filled,
        designSystemR.drawable.feature_pantry_icon_outlined,
        PantryList::class
    ),
    RECIPES(
        recipesR.string.feature_recipes_title,
        recipesR.string.feature_recipes_title,
        designSystemR.drawable.feature_recipes_icon_filled,
        designSystemR.drawable.feature_recipes_icon_outlined,
        RecipesList::class
    ),
    MEAL_PLANNER(
        mealsR.string.feature_meals_icon,
        mealsR.string.feature_meals_title,
        designSystemR.drawable.feature_meal_planner_icon_filled,
        designSystemR.drawable.feature_meal_planner_icon_outlined,
        MealPlanner::class
    ),
}