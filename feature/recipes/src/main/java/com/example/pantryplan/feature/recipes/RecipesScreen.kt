package com.example.pantryplan.feature.recipes

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.example.pantryplan.core.designsystem.R as designSystemR

@Composable
fun RecipesScreen() {
    Box(modifier = Modifier
        .navigationBarsPadding()
        .systemBarsPadding()
        .padding(
            top = dimensionResource(designSystemR.dimen.top_app_bar_height),
            bottom = dimensionResource(designSystemR.dimen.bottom_app_bar_height)
        )
    ) {
        Text(
            text = "Hello Recipes!"
        )
    }
}