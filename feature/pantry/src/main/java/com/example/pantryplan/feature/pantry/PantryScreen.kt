package com.example.pantryplan.feature.pantry

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import com.example.pantryplan.core.designsystem.component.ContentUnavailable
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.pantryplan.core.designsystem.component.MultiFAB
import com.example.pantryplan.core.designsystem.theme.PantryPlanTheme
import com.example.pantryplan.feature.pantry.ui.PantryItemCard
import java.util.UUID
import com.example.pantryplan.core.designsystem.R as designSystemR

@Composable
fun PantryScreen(
    viewModel: PantryViewModel = hiltViewModel(),

    onClickPantryItem: (UUID) -> Unit,
    onCreatePantryItem: () -> Unit
) {
    Scaffold(modifier = Modifier
        .systemBarsPadding()
        .padding(
            top = dimensionResource(designSystemR.dimen.top_app_bar_height),
            bottom = dimensionResource(designSystemR.dimen.bottom_app_bar_height)
        ),
        floatingActionButton = { PantryFABs(onCreatePantryItem) }
    ) { contentPadding ->
        val uiState = viewModel.uiState.collectAsStateWithLifecycle()

        if (uiState.value.pantryItems.isEmpty()) {
            PantryContentUnavailable(modifier = Modifier.padding(contentPadding))
        } else {
            PantryContentList(
                pantryState = uiState.value,
                onClickPantryItem = onClickPantryItem,
                modifier = Modifier.padding(contentPadding)
            )
        }
    }
}

@Composable
internal fun PantryFABs(onCreatePantryItem: () -> Unit) {
    MultiFAB {
        SmallFloatingActionButton(
            onClick = { /* TODO: Navigate to barcode scanning. */ }
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_barcode_scanner),
                contentDescription = stringResource(R.string.feature_pantry_scan_barcode)
            )
        }
        ExtendedFloatingActionButton(
            onClick = onCreatePantryItem,
            icon = { Icon(Icons.Default.Add, "") },
            text = { Text("Add") }
        )
    }
}

@Composable
internal fun PantryContentUnavailable(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = dimensionResource(designSystemR.dimen.horizontal_margin)),
        contentAlignment = Alignment.Center
    ) {
        ContentUnavailable(
            icon = Icons.AutoMirrored.Filled.List,
            title = stringResource(R.string.feature_pantry_empty_error),
            description = stringResource(R.string.feature_pantry_empty_description)
        )
    }
}

@Composable
internal fun PantryContentList(
    pantryState: PantryUiState,
    onClickPantryItem: (UUID) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn (
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(
            vertical = 8.dp,
            horizontal = dimensionResource(designSystemR.dimen.horizontal_margin)
        )
    ) {
        items(pantryState.pantryItems) { item ->
            PantryItemCard(
                item = item,
                onClick = { onClickPantryItem(item.id) }
            )
        }
    }
}

@Preview
@Composable
fun PantryEmptyPreview() {
    PantryPlanTheme {
        Surface {
            PantryContentUnavailable()
        }
    }
}