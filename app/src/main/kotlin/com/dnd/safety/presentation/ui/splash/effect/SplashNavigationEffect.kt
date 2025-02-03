package com.dnd.safety.presentation.ui.splash.effect

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.navOptions
import com.dnd.safety.presentation.navigation.MainBottomNavItem
import com.dnd.safety.presentation.navigation.Route
import com.dnd.safety.presentation.navigation.component.MainNavigator
import kotlinx.coroutines.delay

@Composable
fun SplashNavigationEffect(
    navigator: MainNavigator
) {
    LaunchedEffect(Unit) {
        delay(2000)
        navigator.navigateTo(
            route = Route.Login,
            navOptions = navOptions {
                popUpTo(Route.Splash.route) {
                    inclusive = true
                }
            }
        )
    }
}