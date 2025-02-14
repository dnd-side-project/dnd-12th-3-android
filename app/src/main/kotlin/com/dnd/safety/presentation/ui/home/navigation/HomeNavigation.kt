package com.dnd.safety.presentation.ui.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import com.dnd.safety.presentation.navigation.MainTabRoute
import com.dnd.safety.presentation.navigation.Route
import com.dnd.safety.presentation.ui.home.HomeRoute

fun NavController.navigateToHome(navOptions: NavOptions) {

    val a = navOptions {
        popUpTo(Route.Splash) {
            inclusive = true
        }
    }

    navigate(MainTabRoute.Home, a)
}

fun NavGraphBuilder.homeNavGraph(
) {
    composable<MainTabRoute.Home> {
        HomeRoute()
    }
}