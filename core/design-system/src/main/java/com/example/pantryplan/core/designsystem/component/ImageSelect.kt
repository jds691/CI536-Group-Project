package com.example.pantryplan.core.designsystem.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddAPhoto
import androidx.compose.material.icons.outlined.AddPhotoAlternate
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pantryplan.core.designsystem.R
import com.example.pantryplan.core.designsystem.theme.PantryPlanTheme

@Composable
fun ImageSelect(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    backgroundPainter: (Painter) = painterResource(R.drawable.bigcheese)
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painter = backgroundPainter,
            contentDescription = null,
            modifier = Modifier.matchParentSize(),
            contentScale = ContentScale.Crop,
            colorFilter =  ColorFilter.tint(
                MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.6f),
                blendMode = BlendMode.SrcAtop
            )
        )
        Row (horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            // TODO: Replace with translation strings.
            ImageSelectButton(
                onClick = onClick,
                // TODO: Replace this fuck-ass icon.
                icon = Icons.Outlined.AddAPhoto,
                label = "Take Photo",
            )
            ImageSelectButton(
                onClick = onClick,
                icon = Icons.Outlined.AddPhotoAlternate,
                label = "Pick Photo",
            )
        }
    }
}

@Composable
fun ImageSelectButton(
    onClick: () -> Unit,
    icon: ImageVector,
    label: String,
) {
    Column(
        modifier = Modifier
            .clickable(onClick = onClick)
            .background(
                color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.6f),
                shape = RoundedCornerShape(8.dp),
            )
            .border(
                width = 1.5.dp,
                color = MaterialTheme.colorScheme.outlineVariant,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Image(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(32.dp),
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSecondaryContainer)
        )
        Text(
            text = label,
            color = MaterialTheme.colorScheme.onSecondaryContainer
        )
    }
}

@Preview
@Composable
fun ImageSelectButtonPreview() {
    ImageSelectButton(
        onClick = {},
        icon = Icons.Outlined.AddAPhoto,
        label = "Take Photo",
    )
}

@Preview(widthDp = 400)
@Composable
fun MacrosCardPreviews() {
    PantryPlanTheme {
        ImageSelect(onClick = {}, modifier = Modifier.aspectRatio(1.8f))
    }
}