package com.dnd.safety.presentation.ui.splash.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.dnd.safety.presentation.navigation.Route
import com.dnd.safety.presentation.ui.splash.SplashRoute

fun NavController.navigateToSplash(navOptions: NavOptions) {
    navigate(Route.Splash, navOptions)
}

fun NavGraphBuilder.splashNavGraph(
    onPermissionAllowed: () -> Unit,
) {
    composable<Route.Splash> {
        SplashRoute(
            onPermissionAllowed = onPermissionAllowed
        )
    }
}