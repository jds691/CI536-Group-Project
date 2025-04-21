package com.example.pantryplan.feature.meals

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun MealPlannerScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        NextThreeDays(
            modifier = modifier.padding(horizontal = 16.dp)
        )
    }
}

@Composable
internal fun NextThreeDays(modifier: Modifier = Modifier) {
    var selectedIndex by remember { mutableIntStateOf(0) }
    val options = listOf("Today", "Tomorrow", "Wednesday")

    Column(
        modifier = modifier
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

        when(selectedIndex) {
            0 -> Text("Unable to retrieve upcoming meals for today.")
            1 -> Text("Unable to retrieve upcoming meals for tomorrow.")
            2 -> Text("Unable to retrieve upcoming meals for Wednesday.")

            else -> Text("Unable to retrieve upcoming meals.")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NextThreeDaysPreview() {
    NextThreeDays()
}