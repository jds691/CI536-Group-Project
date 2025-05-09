package com.example.pantryplan.feature.pantry.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import com.example.pantryplan.feature.pantry.PantryItemDetailsScreen
import com.example.pantryplan.feature.pantry.PantryItemEditScreen
import com.example.pantryplan.feature.pantry.PantryScreen
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data object Pantry // Route to Pantry screen
@Serializable
data object PantryList // Route to the root list
@Serializable
data class PantryItemDetails(val id: String) // Route to the details screen

@Serializable
data class PantryItemEdit(val id: String?) // Route to edit an existing item or create a new one


fun NavController.navigateToPantry() = navigate(route = PantryList)
fun NavController.navigateToPantryItem(id: UUID) =
    navigate(route = PantryItemDetails(id = id.toString()))
fun NavController.navigateToPantryItemEdit(id: UUID?) =
    navigate(route = PantryItemEdit(id = id?.toString()))

fun NavGraphBuilder.pantrySection(
    onPantryItemClick: (UUID) -> Unit,
    onPantryItemEdit: (UUID?) -> Unit,
    onBackClick: () -> Unit
) {
    navigation<Pantry>(startDestination = PantryList) {
        composable<PantryList> {
            PantryScreen(
                onClickPantryItem = onPantryItemClick,
                onCreatePantryItem = {
                    onPantryItemEdit(null)
                }
            )
        }

        composable<PantryItemDetails> { entry ->
            val itemDetails = entry.toRoute<PantryItemDetails>()

            PantryItemDetailsScreen(
                id = UUID.fromString(itemDetails.id),
                onBackClick = onBackClick,
                onEditItem = onPantryItemEdit
            )
        }

        composable<PantryItemEdit> { entry ->
            val itemEdit = entry.toRoute<PantryItemEdit>()

            PantryItemEditScreen(
                existingId = if (itemEdit.id != null) UUID.fromString(itemEdit.id) else null,
                onBackClick = onBackClick
            )
        }
    }
}