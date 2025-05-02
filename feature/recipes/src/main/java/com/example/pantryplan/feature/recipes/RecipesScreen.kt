package com.example.pantryplan.feature.recipes

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun RecipesScreen() {
    Box(modifier = Modifier
        .navigationBarsPadding()
        .systemBarsPadding()
        .padding(paddingValues = PaddingValues(top = 64.dp, bottom = 80.dp))) {
        Text(
            text = "Hello Recipes!"
        )
    }
}