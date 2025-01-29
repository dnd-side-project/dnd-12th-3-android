package com.dnd.safety.presentation.ui.splash.effect

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.dnd.safety.presentation.navigation.MainBottomNavItem
import com.dnd.safety.presentation.navigation.component.MainNavigator
import kotlinx.coroutines.delay

@Composable
fun SplashNavigationEffect(
    navigator: MainNavigator,
) {
    LaunchedEffect(Unit) {
        delay(SPLASH_SCREEN_DELAY)
        navigator.navigateTo(MainBottomNavItem.Home)
    }
}

private const val SPLASH_SCREEN_DELAY = 2000L