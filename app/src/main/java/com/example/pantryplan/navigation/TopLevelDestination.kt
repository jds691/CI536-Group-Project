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

enum class TopLevelDestination (
    @StringRes val label: Int,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
){
    PANTRY(R.string.pantry, Icons.Filled.Place, Icons.Outlined.Place),
    RECIPES(R.string.recipes, Icons.Rounded.Bookmark, Icons.Outlined.BookmarkBorder),
    MEAL_PLAN(R.string.mealplan, Icons.Filled.Notifications, Icons.Outlined.Notifications),
}