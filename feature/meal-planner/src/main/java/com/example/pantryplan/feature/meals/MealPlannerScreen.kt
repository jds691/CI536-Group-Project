package com.example.pantryplan.feature.meals

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import java.util.UUID
import com.example.pantryplan.core.designsystem.R as designSystemR

@Composable
fun MealPlannerScreen(
    onRecipeClick: (UUID) -> Unit,
    onMacroCardClick: () -> Unit
) {
    Scaffold(
        modifier = Modifier
        .padding(
            top = dimensionResource(designSystemR.dimen.top_app_bar_height),
            bottom = dimensionResource(designSystemR.dimen.bottom_app_bar_height)
        )
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .padding(contentPadding)
        ) {
            Text(
                text = "Hello Meal Planner!"
            )
            Button(onClick = onMacroCardClick) {
                Text("To nutritional details screen")
            }
        }
    }
}