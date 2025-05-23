package com.dnd.safety.presentation.ui.splash.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.dnd.safety.presentation.navigation.Route
import com.dnd.safety.presentation.navigation.component.MainNavigator
import com.dnd.safety.presentation.ui.splash.SplashRoute

fun NavGraphBuilder.splashNavGraph(
    onPermissionAllowed: () -> Unit,
) {
    composable<Route.Splash> {
        SplashRoute(
            onPermissionAllowed = onPermissionAllowed
        )
    }
}