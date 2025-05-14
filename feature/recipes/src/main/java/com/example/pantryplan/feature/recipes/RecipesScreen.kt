package com.example.pantryplan.feature.recipes

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.GetContent
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
import com.example.pantryplan.core.models.Allergen
import com.example.pantryplan.core.models.NutritionInfo
import com.example.pantryplan.core.models.Recipe
import java.util.EnumSet
import java.util.UUID
import com.example.pantryplan.core.designsystem.R as designSystemR

@Composable
fun RecipesScreen(
    viewModel: RecipesViewModel = hiltViewModel(),

    onClickRecipeItem: (UUID) -> Unit,
    onCreateRecipeItem: () -> Unit
) {
    val recipeUiState: RecipeUiState by viewModel.uiState.collectAsStateWithLifecycle()
    RecipesScreen(
        recipeUiState = recipeUiState,
        onClickRecipeItem = onClickRecipeItem,
        onCreateRecipeItem = onCreateRecipeItem
    )
}

@Composable
internal fun RecipesScreen(
    recipeUiState: RecipeUiState,
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

        if (recipeUiState.recipeItems.isEmpty()) {
            RecipesContentUnavailable(modifier = Modifier.padding(contentPadding))
        } else {
            RecipesContentList(
                recipeState = recipeUiState,
                onClickRecipeItem = onClickRecipeItem,
                modifier = Modifier.padding(contentPadding)
            )
        }
    }
}


@Composable
internal fun RecipesFABs(onCreateRecipeItem: () -> Unit) {
    MultiFAB {
        val context = LocalContext.current
        val importRecipe = rememberLauncherForActivityResult(GetContent()) { uri ->
            if (uri != null) {
                val json = context
                    .contentResolver
                    .openInputStream(uri)!!
                    .bufferedReader()
                    .readText()
                // TODO: call out to the repository once it's in RecipesViewModel.
            }
        }

        SmallFloatingActionButton(
            onClick = { importRecipe.launch("application/json") }
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
                userAllergies = recipeState.allergies,
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

@Preview
@Composable
fun RecipesScreenPreview() {
    PantryPlanTheme {
        Surface {
            RecipesScreen(
                recipeUiState = RecipeUiState(
                    recipeItems = listOf(
                        Recipe(
                            id = UUID.randomUUID(),
                            title = "Cheeseburger",
                            description = "Burger packed with juicy beef, melted cheese and extra vegetables to add that final flavour.",
                            tags = listOf("Dinner", "High Protein"),
                            allergens = EnumSet.of(Allergen.MILK, Allergen.GLUTEN, Allergen.SESAME),
                            imageUrl = null,
                            instructions = listOf("1. Cook Burger", "2. Eat burger"),
                            ingredients = emptyList(),
                            prepTime = 10f,
                            cookTime = 15f,
                            nutrition = NutritionInfo(
                                calories = 100,
                                fats = 100f,
                                saturatedFats = 100f,
                                carbohydrates = 100f,
                                sugar = 100f,
                                fiber = 100f,
                                protein = 100f,
                                sodium = 100f
                            )
                        )
                    )
                ),
                onClickRecipeItem = {},
                onCreateRecipeItem = {}
            )
        }
    }
}
