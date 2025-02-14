package com.dnd.safety.presentation.ui.postreport.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.dnd.safety.presentation.navigation.MainTabRoute
import com.dnd.safety.presentation.navigation.Route
import com.dnd.safety.presentation.ui.mytown.MyTownRoute
import com.dnd.safety.presentation.ui.postreport.PostReportScreen

fun NavController.navigateToPostReport() {
    navigate(MainTabRoute.PostReport.Camera)
}

fun NavGraphBuilder.postReportNavGraph(
    onGoBack: () -> Unit,
) {
    composable<MainTabRoute.PostReport.Camera> {
        PostReportScreen(
            onGoBack = onGoBack,
        )
    }
}