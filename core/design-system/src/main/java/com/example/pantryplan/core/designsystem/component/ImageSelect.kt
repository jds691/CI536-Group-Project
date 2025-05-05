package com.example.pantryplan.core.designsystem.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
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
        modifier = modifier.paint(
            painter = backgroundPainter,
            contentScale = ContentScale.FillWidth,
            colorFilter = ColorFilter.tint(
                MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.6f),
                blendMode = BlendMode.SrcAtop
            )
        ).clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Image(
            imageVector = Icons.Outlined.CameraAlt,
            contentDescription = "Select image",
            modifier = Modifier.size(54.dp),
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSecondaryContainer)
        )
    }
}

@Preview(widthDp = 400)
@Composable
fun MacrosCardPreviews() {
    PantryPlanTheme {
        ImageSelect(onClick = {})
    }
}