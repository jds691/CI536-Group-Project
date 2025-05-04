package com.example.pantryplan.feature.recipes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.pantryplan.core.designsystem.component.ContentUnavailable
import com.example.pantryplan.core.designsystem.component.MultiFAB
import com.example.pantryplan.core.designsystem.recipes.RecipeItemCard
import com.example.pantryplan.core.designsystem.theme.PantryPlanTheme
import java.util.UUID
import com.example.pantryplan.core.designsystem.R as designSystemR

@Composable
fun RecipesScreen(
    viewModel: RecipesViewModel = hiltViewModel(),

    onClickRecipeItem: (UUID) -> Unit,
    onCreateRecipeItem: () -> Unit
) {
    Scaffold(
        modifier = Modifier
            .systemBarsPadding()
            .padding(
                top = dimensionResource(designSystemR.dimen.top_app_bar_height),
                bottom = dimensionResource(designSystemR.dimen.bottom_app_bar_height)
            ),
        floatingActionButton = { RecipesFABs(onCreateRecipeItem) }
    ) { contentPadding ->
        val uiState = viewModel.uiState.collectAsStateWithLifecycle()

        if (uiState.value.recipeItems.isEmpty()) {
            RecipesContentUnavailable(modifier = Modifier.padding(contentPadding))
        } else {
            RecipesContentList(
                recipeState = uiState.value,
                onClickRecipeItem = onClickRecipeItem,
                modifier = Modifier.padding(contentPadding)
            )
        }
    }
}

@Composable
internal fun RecipesFABs(onCreateRecipeItem: () -> Unit) {
    MultiFAB {
        SmallFloatingActionButton(
            onClick = { /* TODO: Recipe download stuff. */ }
        ) {
            Icon(
                painter = painterResource(R.drawable.download_symbol),
                contentDescription = stringResource(R.string.feature_recipes_download_recipe)
            )
        }
        ExtendedFloatingActionButton(
            onClick = onCreateRecipeItem,
            icon = { Icon(Icons.Default.Add, "") },
            text = { Text("Add") }
        )
    }
}

@Composable
internal fun RecipesContentUnavailable(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = dimensionResource(designSystemR.dimen.horizontal_margin)),
        contentAlignment = Alignment.Center
    ) {
        ContentUnavailable(
            icon = Icons.AutoMirrored.Filled.List,
            title = stringResource(R.string.feature_recipes_empty_error),
            description = stringResource(R.string.feature_recipes_empty_description)
        )
    }
}

@Composable
internal fun RecipesContentList(
    recipeState: RecipeUiState,
    onClickRecipeItem: (UUID) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(
            vertical = 8.dp,
            horizontal = dimensionResource(designSystemR.dimen.horizontal_margin)
        )
    ) {
        items(recipeState.recipeItems) { item ->
            RecipeItemCard(
                item = item,
                onClick = { onClickRecipeItem(item.id) }
            )
        }
    }
}

@Preview
@Composable
fun RecipesEmptyPreview() {
    PantryPlanTheme {
        Surface {
            RecipesContentUnavailable()
        }
    }
}