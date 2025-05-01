package com.example.pantryplan.feature.pantry

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import com.example.pantryplan.core.designsystem.component.ContentUnavailable
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.pantryplan.core.designsystem.component.ContentUnavailable
import com.example.pantryplan.core.designsystem.theme.PantryPlanTheme
import java.util.UUID
import com.example.pantryplan.core.designsystem.R as designSystemR

@Composable
fun PantryScreen(
    viewModel: PantryViewModel = hiltViewModel(),

    onClickPantryItem: (UUID) -> Unit,
    onCreatePantryItem: () -> Unit
) {
    Scaffold(modifier = Modifier
        .padding(
            top = dimensionResource(designSystemR.dimen.top_app_bar_height),
            bottom = dimensionResource(designSystemR.dimen.bottom_app_bar_height)
        ),
        floatingActionButton = { PantryFABs() }
    ) { contentPadding ->
        val uiState = viewModel.uiState.collectAsStateWithLifecycle()

        if (uiState.value.pantryItems.isEmpty()) {
            PantryContentUnavailable(modifier = Modifier.padding(contentPadding))
        } else {
            // TODO: Show a list of all cards
            /*
            PantryItemCard(
                item = pantryItem,
                onClick = {
                    onClickPantryItem(pantryItem.id)
                }
            )*/
        }
    }
}

@Composable
internal fun PantryFABs() {
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
            onClick = { /* TODO: Navigate to pantry item creation. */ },
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

@Preview
@Composable
fun PantryEmptyPreview() {
    PantryPlanTheme {
        Surface {
            PantryContentUnavailable()
        }
    }
}