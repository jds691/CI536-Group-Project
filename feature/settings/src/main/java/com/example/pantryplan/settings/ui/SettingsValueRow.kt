package com.example.pantryplan.settings.ui

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
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
fun SettingsValueRow(
    title: String,
    modifier: Modifier = Modifier,
    description: String? = null,
    content: @Composable RowScope.() -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 56.dp)
            .padding(vertical = 8.dp)
    ) {
        SettingsValueRowBody(
            title = title,
            description = description
        )

        content()
    }
}

@Composable
fun SettingsValueRow(
    title: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    description: String? = null,
    content: @Composable RowScope.() -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 56.dp)
            .padding(vertical = 8.dp)
    ) {
        SettingsValueRowBody(
            title = title,
            icon = icon,
            description = description
        )

        content()
    }
}

@Composable
fun SettingsValueRow(
    title: String,
    @DrawableRes icon: Int,
    modifier: Modifier = Modifier,
    description: String? = null,
    content: @Composable RowScope.() -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 56.dp)
            .padding(vertical = 8.dp)
    ) {
        SettingsValueRowBody(
            title = title,
            icon = icon,
            description = description
        )

        content()
    }
}

@Composable
private fun SettingsValueRowBody(
    title: String,
    modifier: Modifier = Modifier,
    description: String? = null,
) {
    Column(
        modifier = modifier
    ) {
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

@Composable
private fun SettingsValueRowBody(
    title: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    description: String? = null,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.Start),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Icon(imageVector = icon, "")
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
}

@Composable
private fun SettingsValueRowBody(
    title: String,
    @DrawableRes icon: Int,
    modifier: Modifier = Modifier,
    description: String? = null,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.Start),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Icon(painter = painterResource(icon), "")
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
}

@Preview(showBackground = true)
@Composable
private fun SettingsValueRowPreview() {
    PantryPlanTheme {
        SettingsValueRow(
            title = "Allergies",
            description = "Unknown amount"
        ) {
            Text("Test")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingsValueRowWithIconPreview() {
    PantryPlanTheme {
        SettingsValueRow(
            title = "Allergies",
            description = "Unknown amount",
            icon = Icons.Default.Settings
        ) {
            Text("Test")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingsValueRowWithIconNoDescriptionPreview() {
    PantryPlanTheme {
        SettingsValueRow(
            title = "Allergies",
            icon = Icons.Default.Settings
        ) {
            Text("Test")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingsValueRowWithNoDecorationPreview() {
    PantryPlanTheme {
        SettingsValueRow(
            title = "Allergies"
        ) {
            Text("Test")
        }
    }
}