package com.dnd.safety.presentation.ui.postreport.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.dnd.safety.domain.model.Incident
import com.dnd.safety.presentation.navigation.MainTabRoute
import com.dnd.safety.presentation.navigation.utils.composableType
import com.dnd.safety.presentation.ui.postreport.CameraScreen
import com.dnd.safety.presentation.ui.postreport.PostReportMode
import com.dnd.safety.presentation.ui.postreport.PostReportScreen

fun NavController.navigateToPostReportCamera() {
    navigate(MainTabRoute.PostReport.Camera)
}

fun NavController.navigateToPostReport(uri: String) {
    navigate(MainTabRoute.PostReport.Report(uri))
}

fun NavController.navigateToPostReportEdit(incident: Incident) {
    navigate(MainTabRoute.PostReport.Edit(incident))
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
            onGoBack = onGoBack,
        )
    }
    composable<MainTabRoute.PostReport.Report> {
        PostReportScreen(
            postReportMode = PostReportMode.CREATE,
            onGoBackToHome = onGoBackToHome,
            onShowSnackBar = onShowSnackBar,
        )
    }
    composableType<MainTabRoute.PostReport.Edit, Incident> {
        PostReportScreen(
            postReportMode = PostReportMode.EDIT,
            onGoBackToHome = onGoBackToHome,
            onShowSnackBar = onShowSnackBar,
        )
    }
}