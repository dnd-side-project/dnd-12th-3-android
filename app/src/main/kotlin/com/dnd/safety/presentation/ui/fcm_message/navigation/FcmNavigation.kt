package com.dnd.safety.presentation.ui.fcm_message.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.dnd.safety.presentation.navigation.Route
import com.dnd.safety.presentation.ui.fcm_message.FcmMessageRoute

fun NavController.navigateToFcmMessage() {
    navigate(Route.FcmMessage)
}

fun NavGraphBuilder.fcmMessageNavGraph(
    onGoBack: () -> Unit
) {
    composable<Route.FcmMessage> {
        FcmMessageRoute(
            onGoBack = onGoBack
        )
    }
}