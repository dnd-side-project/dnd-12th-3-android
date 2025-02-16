package com.dnd.safety.presentation.ui.location_search.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.dnd.safety.domain.model.MyTown
import com.dnd.safety.presentation.navigation.Route
import com.dnd.safety.presentation.ui.location_search.LocationConfirmScreen
import com.dnd.safety.presentation.ui.location_search.LocationSearchScreen
import com.google.android.gms.maps.model.LatLng

fun NavController.navigateToLocationSearch() {
    navigate(Route.SearchLocation)
}

fun NavController.navigationToLocationConfirm(
    myTown: MyTown
) {
    navigate(
        Route.LocationConfirm(
            title = myTown.title,
            address = myTown.address,
            pointX = myTown.point.x.toString(),
            pointY = myTown.point.y.toString(),
        )
    )
}

fun NavGraphBuilder.locationNavGraph(
    onGoBack: () -> Unit,
    onComplete: () -> Unit,
    onShowNavigationConfirm: (MyTown) -> Unit,
) {
    composable<Route.SearchLocation> {
        LocationSearchScreen(
            onGoBack = onGoBack,
            onShowNavigationConfirm = onShowNavigationConfirm,
        )
    }
    composable<Route.LocationConfirm> { backStackEntry ->
        val myTown = backStackEntry.toRoute<Route.LocationConfirm>()
        LocationConfirmScreen(
            address = myTown.title,
            location = LatLng(myTown.pointY.toDouble(), myTown.pointX.toDouble()),
            onGoBack = onGoBack,
            onComplete = onComplete,
        )
    }
}