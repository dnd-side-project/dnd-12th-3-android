package com.dnd.safety.presentation.ui.detail.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.dnd.safety.domain.model.Incident
import com.dnd.safety.presentation.navigation.Route
import com.dnd.safety.presentation.navigation.utils.composableType
import com.dnd.safety.presentation.ui.detail.DetailRoute

fun NavController.navigateToDetail(incident: Incident) {
    navigate(Route.IncidentDetail(incident.id))
}


fun NavGraphBuilder.detailNavGraph(
    onGoBack: () -> Unit,
    onShowSnackBar: (String) -> Unit,
    onShowIncidentEdit: (Incident) -> Unit,
) {
    composable<Route.IncidentDetail> {
        DetailRoute(
            onGoBack = onGoBack,
            onShowSnackBar = onShowSnackBar,
            onShowIncidentEdit = onShowIncidentEdit
        )
    }
}