package com.dnd.safety.presentation.ui.postreport.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.dnd.safety.presentation.navigation.MainTabRoute
import com.dnd.safety.presentation.ui.postreport.CameraScreen
import com.dnd.safety.presentation.ui.postreport.PostReportScreen

fun NavController.navigateToPostReportCamera(navOptions: NavOptions) {
    navigate(MainTabRoute.PostReport.Camera, navOptions)
}

fun NavController.navigateToPostReport(uri: String) {
    navigate(MainTabRoute.PostReport.Report(uri))
}

fun NavGraphBuilder.postReportNavGraph(
    onGoBack: () -> Unit,
    onGoBackToHome: () -> Unit,
    onShowSnackBar: (String) -> Unit,
    onShowPostReport: (String) -> Unit,
) {
    composable<MainTabRoute.PostReport.Camera> {
        CameraScreen(
            onCameraCapture = onShowPostReport,
            onShowSnackBar = onShowSnackBar,
        )
    }
    composable<MainTabRoute.PostReport.Report> {
        PostReportScreen(
            onGoBackToHome = onGoBackToHome,
            onShowSnackBar = onShowSnackBar,
        )
    }
}