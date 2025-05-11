package com.example.pantryplan.feature.pantry

import android.content.Context
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.pantryplan.core.designsystem.component.ContentUnavailable
import com.example.pantryplan.core.designsystem.component.MultiFAB
import com.example.pantryplan.core.designsystem.theme.PantryPlanTheme
import com.example.pantryplan.core.models.PantryItem
import com.example.pantryplan.core.models.PantryItemState
import com.example.pantryplan.feature.pantry.ui.PantryItemCard
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import kotlinx.datetime.Clock
import java.util.UUID
import kotlin.time.Duration.Companion.days
import com.example.pantryplan.core.designsystem.R as designSystemR

@Composable
fun PantryScreen(
    viewModel: PantryViewModel = hiltViewModel(),

    onClickPantryItem: (UUID) -> Unit,
    onCreatePantryItem: () -> Unit,
    onScanBarcode: (String) -> Unit,
) {
    val context = LocalContext.current

    Scaffold(modifier = Modifier
        .systemBarsPadding()
        .padding(
            top = dimensionResource(designSystemR.dimen.top_app_bar_height),
            bottom = dimensionResource(designSystemR.dimen.bottom_app_bar_height)
        ),
        floatingActionButton = {
            PantryFABs(
                onScanBarcode = {
                    scanBarcode(
                        context = context,
                        onSuccess = { onScanBarcode(it.rawValue.toString()) }
                    )
                },
                onCreatePantryItem = onCreatePantryItem
            )
        }
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

private fun scanBarcode(
    context: Context,
    onSuccess: (Barcode) -> Unit = {},
    onCancelled: () -> Unit = {},
    onFailure: (Exception) -> Unit = {},
) {
    val options = GmsBarcodeScannerOptions
        .Builder()
        .setBarcodeFormats(
            Barcode.FORMAT_UPC_A, Barcode.FORMAT_UPC_E
        )
        .build()
    val scanner = GmsBarcodeScanning.getClient(context, options)
    scanner
        .startScan()
        .addOnSuccessListener(onSuccess)
        .addOnCanceledListener(onCancelled)
        .addOnFailureListener(onFailure)
}

@Composable
private fun PantryFABs(
    onScanBarcode: () -> Unit,
    onCreatePantryItem: () -> Unit
) {
    MultiFAB {
        SmallFloatingActionButton(
            onClick = onScanBarcode
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
private fun PantryContentUnavailable(modifier: Modifier = Modifier) {
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
private fun PantryContentList(
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
private fun PantryPopulatedPreview(
    @PreviewParameter(SamplePantryItemProvider::class) pantryItems: List<PantryItem>
) {
    PantryPlanTheme {
        Surface {
            PantryContentList(
                pantryState = PantryUiState(pantryItems),
                onClickPantryItem = {}
            )
        }
    }
}

@Preview
@Composable
private fun PantryEmptyPreview() {
    PantryPlanTheme {
        Surface {
            PantryContentUnavailable()
        }
    }
}

private class SamplePantryItemProvider : PreviewParameterProvider<List<PantryItem>> {
    override val values: Sequence<List<PantryItem>> = sequenceOf(listOf(
        PantryItem(
            id = UUID.randomUUID(),
            name = "Bacon",
            quantity = 1000,
            expiryDate = Clock.System.now(),
            expiresAfter = 7.5.days,
            inStateSince = Clock.System.now(),
            state = PantryItemState.SEALED,
            imageUrl = null,
            barcode = null
        ),
        PantryItem(
            id = UUID.randomUUID(),
            name = "Cheese",
            quantity = 1000,
            expiryDate = Clock.System.now(),
            expiresAfter = 1.days,
            inStateSince = Clock.System.now(),
            state = PantryItemState.OPENED,
            imageUrl = null,
            barcode = null
        ),
        PantryItem(
            id = UUID.randomUUID(),
            name = "Milk",
            quantity = 1000,
            expiryDate = Clock.System.now(),
            expiresAfter = 1.days,
            inStateSince = Clock.System.now(),
            state = PantryItemState.EXPIRED,
            imageUrl = null,
            barcode = null
        ),
        PantryItem(
            id = UUID.randomUUID(),
            name = "Pasta Bake",
            quantity = 1000,
            expiryDate = Clock.System.now(),
            expiresAfter = 7.days,
            inStateSince = Clock.System.now(),
            state = PantryItemState.FROZEN,
            imageUrl = null,
            barcode = null
        ),
        PantryItem(
            id = UUID.randomUUID(),
            name = "Egg",
            quantity = 1000,
            expiryDate = Clock.System.now(),
            expiresAfter = Int.MAX_VALUE.days,
            inStateSince = Clock.System.now(),
            state = PantryItemState.SEALED,
            imageUrl = null,
            barcode = null
        ),
    ))
}
