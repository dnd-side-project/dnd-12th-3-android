package com.dnd.safety.presentation.designsystem.theme

import android.app.Activity
import android.os.Build
import android.view.View
import android.view.WindowInsetsController
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView

private val LightColorScheme = lightColorScheme(
    primary = Main,
    surfaceDim = Background,
    onSurface = White,
    surface = Background,
    background = Gray80, // 항상 Gray80 사용
)

@Composable
fun SafetyTheme(
    content: @Composable () -> Unit
) {
    val statusBarColor = Gray80

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = statusBarColor.toArgb()

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                val controller = window.decorView.windowInsetsController
                controller?.setSystemBarsAppearance(
                    0,
                    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                )
            } else {
                @Suppress("DEPRECATION")
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
            }
        }
    }

    CompositionLocalProvider(
        LocalTypography provides Typography,
    ) {
        MaterialTheme(
            colorScheme = LightColorScheme,
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