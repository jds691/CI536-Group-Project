@file:OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)

package com.example.pantryplan.feature.recipes.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pantryplan.feature.recipes.R

@Composable
fun RecipeIngredientCard(
    modifier: Modifier = Modifier,

    onClick: () -> Unit = {},
) {

    //TODO Will pass in recipe ingredient amount and pantry item amount
    val gramsNeeded = 100
    val gramsOfItem = 600

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = CardDefaults.elevatedShape
            ),
        onClick = onClick,
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.Start),
            verticalAlignment = Alignment.CenterVertically
        ) {
            //if (item.imageUrl != null) {
            //TODO: Place recipe ingredient image in card when there is an active image passed in
            //} else {
            Image(
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(1.0f),
                painter = painterResource(R.drawable.beefburgers),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
            //}
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 9.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.Top),
                horizontalAlignment = Alignment.Start,
            ) {

                val progressAmount = gramsNeeded.toFloat() / gramsOfItem.toFloat()
                var progressColor = Color.Green

                Text(
                    text = "Beef Burger",
                    style = MaterialTheme.typography.titleMedium
                )
                if (progressAmount < 1.0f) {
                    Text(
                        text = "In pantry: " + gramsOfItem + "g - Uses: " + gramsNeeded + "g",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                } else {
                    Text(
                        text = "! Not enough | In pantry: " + gramsOfItem + "g - Uses: " + gramsNeeded + "g",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error
                    )
                    progressColor = MaterialTheme.colorScheme.error
                }

                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth(),
                    progress = { progressAmount },
                    color = progressColor,
                    trackColor = progressColor.copy(0.2f)
                )
            }
        }
    }
}

@Preview
@Composable
fun RecipeIngredientCardPreview() {
    RecipeIngredientCard()
}
