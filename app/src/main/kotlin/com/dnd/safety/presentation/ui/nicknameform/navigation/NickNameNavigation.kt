package com.dnd.safety.presentation.ui.nicknameform.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.dnd.safety.presentation.navigation.Route
import com.dnd.safety.presentation.ui.mytown.MyTownRoute
import com.dnd.safety.presentation.ui.nicknameform.NicknameFormScreen

fun NavController.navigateToNickName() {
    navigate(Route.NicknameForm)
}

fun NavGraphBuilder.nickNameNavGraph(
    onShowSearchLocation: (String) -> Unit,
) {
    composable<Route.NicknameForm> {
        NicknameFormScreen(
            onShowSearchLocation = onShowSearchLocation,
        )
    }
}