package com.dnd.safety.presentation.ui.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.dnd.safety.presentation.navigation.MainTab
import com.dnd.safety.presentation.navigation.MainTabRoute
import com.dnd.safety.presentation.ui.home.HomeRoute

fun NavController.navigateToHome(navOptions: NavOptions) {
    navigate(MainTabRoute.Home, navOptions)
}

fun NavGraphBuilder.homeNavGraph(
    onBottomNavClicked: (MainTab) -> Unit
) {
    composable<MainTabRoute.Home> {
        HomeRoute(
            onBottomNavClicked = onBottomNavClicked
        )
    }
}