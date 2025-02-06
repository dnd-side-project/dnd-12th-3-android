package com.dnd.safety.presentation.designsystem.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

private val LightColorScheme = lightColorScheme(
    primary = Main,
    surfaceDim = Background,
    onSurface = White,
    surface = Background,
)

@Composable
fun SafetyTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = LightColorScheme

    MaterialTheme.typography.bodyLarge

    CompositionLocalProvider(
        LocalTypography provides Typography,
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            content = content
        )
    }
}

object SafetyTheme {

    val typography: SafetyTypography
        @Composable
        get() = LocalTypography.current

    val shapes: SafetyShapes
        @Composable
        get() = LocalShapes.current
}