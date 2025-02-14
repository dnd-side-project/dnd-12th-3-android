package com.dnd.safety.presentation.ui.login.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import com.dnd.safety.presentation.navigation.Route
import com.dnd.safety.presentation.ui.login.LoginScreen
import com.dnd.safety.presentation.ui.mytown.MyTownRoute

fun NavController.navigateToLogin() {
    val navOptions = navOptions {
        popUpTo(Route.Splash) {
            inclusive = true
        }
    }
    navigate(Route.Login, navOptions)
}

fun NavGraphBuilder.loginNavGraph(
    onShowNickName: () -> Unit,
) {
    composable<Route.Login> {
        LoginScreen(
            onShowNickName = onShowNickName
        )
    }
}