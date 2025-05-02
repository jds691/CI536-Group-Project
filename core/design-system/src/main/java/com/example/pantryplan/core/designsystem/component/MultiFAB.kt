package com.example.pantryplan.core.designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MultiFAB(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalAlignment = Alignment.End
    ) {
        content()
    }
}