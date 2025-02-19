package com.dnd.safety.presentation.ui.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.dnd.safety.domain.model.Incident
import com.dnd.safety.presentation.navigation.MainTab
import com.dnd.safety.presentation.navigation.MainTabRoute
import com.dnd.safety.presentation.ui.home.HomeRoute

fun NavController.navigateToHome(navOptions: NavOptions) {
    navigate(MainTabRoute.Home, navOptions)
}


fun NavGraphBuilder.homeNavGraph(
    onShowDetail: (Incident) -> Unit,
    onBottomNavClicked: (MainTab) -> Unit,
    onShowSnackBar: (String) -> Unit,
) {
    composable<MainTabRoute.Home> {
        HomeRoute(
            onIncidentDetail = onShowDetail,
            onBottomNavClicked = onBottomNavClicked,
            onShowSnackBar = onShowSnackBar
        )
    }
}