package com.dnd.safety.presentation.ui.myPage.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.dnd.safety.presentation.navigation.MainTabRoute
import com.dnd.safety.presentation.ui.myPage.MyPageRoute
import com.dnd.safety.presentation.ui.myPage.MyReportRoute
import com.dnd.safety.presentation.ui.myPage.MyTownRoute

fun NavController.navigateToMyPage(navOptions: NavOptions) {
    navigate(MainTabRoute.MyPage.Home, navOptions)
}

fun NavController.navigateToMyReport() {
    navigate(MainTabRoute.MyPage.MyReport)
}

fun NavController.navigateToMyTown() {
    navigate(MainTabRoute.MyPage.MyTown)
}

fun NavController.navigateToSignOut() {
    navigate(MainTabRoute.MyPage.SingOut)
}

fun NavGraphBuilder.myPageNavGraph(
    onGoBack: () -> Unit,
    onShowMyReport: () -> Unit,
    onShowMyTown: () -> Unit,
    onShowSingOut: () -> Unit
) {
    composable<MainTabRoute.MyPage.Home> {
        MyPageRoute(
            onGoBack = onGoBack,
            onShowMyReport = onShowMyReport,
            onShowMyTown = onShowMyTown,
            onShowSingOut = onShowSingOut
        )
    }

    composable<MainTabRoute.MyPage.MyReport> {
        MyReportRoute(
            onGoBack = onGoBack,
        )
    }

    composable<MainTabRoute.MyPage.MyTown> {
        MyTownRoute()
    }

    composable<MainTabRoute.MyPage.SingOut> {
        MyTownRoute()
    }
}