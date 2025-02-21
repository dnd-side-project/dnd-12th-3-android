package com.dnd.safety.presentation.ui.myPage.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.dnd.safety.domain.model.Incident
import com.dnd.safety.presentation.navigation.MainTabRoute
import com.dnd.safety.presentation.ui.myPage.MyPageRoute
import com.dnd.safety.presentation.ui.myPage.MyReportRoute
import com.dnd.safety.presentation.ui.myPage.MyTownRoute
import com.dnd.safety.presentation.ui.myPage.SignOutRoute

fun NavController.navigateToMyPage() {
    navigate(MainTabRoute.MyPage.Home)
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
    onShowSingOut: () -> Unit,
    onShowSnackBar: (String) -> Unit,
    onShowIncidentEdit: (Incident) -> Unit
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
            onShowSnackBar = onShowSnackBar,
            onShowIncidentEdit = onShowIncidentEdit
        )
    }

    composable<MainTabRoute.MyPage.MyTown> {
        MyTownRoute(
            onGoBack = onGoBack,
            onShowSnackBar = onShowSnackBar
        )
    }

    composable<MainTabRoute.MyPage.SingOut> {
        SignOutRoute(
            onGoBack = onGoBack,
            onShowSnackBar = onShowSnackBar
        )
    }
}