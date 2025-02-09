package com.dnd.safety.presentation.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun WatchOutLoadingIndicator(
    isLoading: Boolean,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color.Black.copy(alpha = 0.4f),
    indicatorColor: Color = MaterialTheme.colorScheme.primary,
    indicatorSize: Dp = 48.dp,
) {
    if (isLoading) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(backgroundColor),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(indicatorSize),
                color = indicatorColor
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun WatchOutLoadingIndicatorPreview() {
    Box(modifier = Modifier.fillMaxSize()) {
        WatchOutLoadingIndicator(
            isLoading = true,
            backgroundColor = Color.Black.copy(alpha = 0.4f),
            indicatorColor = MaterialTheme.colorScheme.primary
        )
    }
}