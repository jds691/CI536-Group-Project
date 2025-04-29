package com.example.pantryplan.feature.meals.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pantryplan.core.models.NutritionInfo
import kotlin.math.roundToInt

@OptIn(ExperimentalFoundationApi::class)

@Composable
fun MacrosCard(
    item: NutritionInfo,

    onClick: () -> Unit = {}
) {
    //4 calories per gram of protein
    val proteinPercentage = (item.protein * 4 / item.calories * 1000).roundToInt() / 10f
    //4 calories per gram of carbs
    val carbohydratePercentage = (item.carbohydrates * 4 / item.calories * 1000).roundToInt() / 10f
    //9 calories per gram of fat
    val fatPercentage = (item.fats * 9 / item.calories * 1000).roundToInt() / 10f

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .combinedClickable(
                onClick = onClick)
    ) {
        Column (
            modifier = Modifier
                .background(Color(0xFFEADDFF))
                .padding(12.dp)
        ) {
            Row{
                Text(
                    text = "Protein",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    modifier = Modifier.weight(proteinPercentage),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    softWrap = false

                )
                Text(
                    text = "Carbs",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    modifier = Modifier.weight(carbohydratePercentage),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    softWrap = false
                )
                Text(
                    text = "Fats",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    modifier = Modifier.weight(fatPercentage),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    softWrap = false
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(28.dp) // 👈 height of the bar
                    .clip(RoundedCornerShape(8.dp))
            ) {
                Box(

                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .weight(proteinPercentage)
                        .fillMaxHeight()
                        .background(Color(0xFFFFDAD6))

                ) {

                    Text(
                        fontSize = 12.sp,
                        text = "${proteinPercentage}%"
                    )
                }

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .weight(carbohydratePercentage)
                        .fillMaxHeight()
                        .background(Color(0xFFF9E287))
                ) {
                    Text(
                        fontSize = 12.sp,
                        text = "${carbohydratePercentage}%"
                    )
                }
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .weight(fatPercentage)
                        .fillMaxHeight()
                        .background(Color(0xFF9CF1ED))
                ) {
                    Text(
                        fontSize = 12.sp,
                        text = "${fatPercentage}%"
                    )
                }

            }
            Row{
                Text(
                    text = "${item.protein}g",
                    fontSize = 12.sp,
                    modifier = Modifier.weight(proteinPercentage),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    softWrap = false

                )
                Text(
                    text = "${item.carbohydrates}g",
                    fontSize = 12.sp,
                    modifier = Modifier.weight(carbohydratePercentage),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    softWrap = false
                )
                Text(
                    text = "${item.fats}g",
                    fontSize = 12.sp,
                    modifier = Modifier.weight(fatPercentage),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    softWrap = false
                )
            }
        }
    }
}
