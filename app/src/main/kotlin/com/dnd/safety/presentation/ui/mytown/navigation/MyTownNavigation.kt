package com.dnd.safety.presentation.ui.mytown.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.dnd.safety.presentation.navigation.Route
import com.dnd.safety.presentation.ui.home.HomeRoute
import com.dnd.safety.presentation.ui.mytown.MyTownRoute

fun NavController.navigateToMyTown() {
    navigate(Route.MyTown)
}

fun NavGraphBuilder.myTownNavGraph(
) {
    composable<Route.MyTown> {
        MyTownRoute()
    }
}