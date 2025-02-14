package com.dnd.safety.presentation.ui.location_search.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.dnd.safety.data.model.Location
import com.dnd.safety.presentation.navigation.Route
import com.dnd.safety.presentation.navigation.utils.composableType
import com.dnd.safety.presentation.ui.location_search.LocationConfirmScreen
import com.dnd.safety.presentation.ui.location_search.LocationSearchScreen
import com.dnd.safety.presentation.ui.nicknameform.NicknameFormScreen

fun NavController.navigateToLocationSearch(nickname: String) {
    navigate(Route.SearchLocation(nickname))
}

fun NavController.navigationToLocationConfirm(
    nickname: String,
    location: Location,
) {
    navigate(Route.LocationConfirm(nickname, location))
}

fun NavGraphBuilder.locationNavGraph(
    onShowNavigationConfirm: (String, Location) -> Unit,
) {
    composable<Route.NicknameForm> {
        LocationSearchScreen(
            onShowNavigationConfirm = onShowNavigationConfirm,
        )
    }
    composableType<Route.LocationConfirm, Location> {
        LocationConfirmScreen(
        )
    }
}