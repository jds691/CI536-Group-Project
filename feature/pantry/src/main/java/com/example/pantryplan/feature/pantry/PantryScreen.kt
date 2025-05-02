package com.example.pantryplan.feature.pantry

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.pantryplan.core.designsystem.component.ContentUnavailable
import java.util.UUID
import com.example.pantryplan.core.designsystem.R as designSystemR

@Composable
fun PantryScreen(
    viewModel: PantryViewModel = hiltViewModel(),

    onClickPantryItem: (UUID) -> Unit,
    onCreatePantryItem: () -> Unit
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    if (uiState.value.pantryItems.isEmpty()) {
        PantryContentUnavailable()
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

@Composable
internal fun PantryContentUnavailable() {
    Box(
        modifier = Modifier
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
    MaterialTheme {
        Surface {
            PantryContentUnavailable()
        }
    }
}