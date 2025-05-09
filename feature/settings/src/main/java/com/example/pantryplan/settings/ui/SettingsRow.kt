package com.example.pantryplan.settings.ui

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pantryplan.core.designsystem.theme.PantryPlanTheme

@Composable
fun SettingsRow(
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    description: String? = null
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.Start),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 56.dp)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        SettingsRowBody(
            title = title,
            description = description
        )
    }
}

@Composable
fun SettingsRow(
    title: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    description: String? = null
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.Start),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 56.dp)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Icon(imageVector = icon, "")
        SettingsRowBody(
            title = title,
            description = description
        )
    }
}

@Composable
fun SettingsRow(
    title: String,
    @DrawableRes icon: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    description: String? = null
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.Start),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 56.dp)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Icon(painter = painterResource(icon), "")
        SettingsRowBody(
            title = title,
            description = description
        )
    }
}

@Composable
private fun SettingsRowBody(
    title: String,
    description: String? = null
) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge
        )
        if (description != null) {
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingsRowPreview() {
    PantryPlanTheme {
        SettingsRow(
            title = "Allergies",
            description = "Unknown amount",

            onClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingsRowWithIconPreview() {
    PantryPlanTheme {
        SettingsRow(
            title = "Allergies",
            description = "Unknown amount",
            icon = Icons.Default.Settings,

            onClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingsRowWithIconNoDescriptionPreview() {
    PantryPlanTheme {
        SettingsRow(
            title = "Allergies",
            icon = Icons.Default.Settings,

            onClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingsRowWithNoDecorationPreview() {
    PantryPlanTheme {
        SettingsRow(
            title = "Allergies",

            onClick = {}
        )
    }
}