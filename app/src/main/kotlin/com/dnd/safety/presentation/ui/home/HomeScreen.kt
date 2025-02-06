package com.dnd.safety.presentation.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dnd.safety.presentation.ui.home.component.HomeBottomSheetScaffold
import com.dnd.safety.presentation.ui.home.component.HomeMapView
import com.dnd.safety.presentation.ui.home.component.IncidentList
import com.dnd.safety.presentation.ui.home.state.IncidentsState
import com.google.android.gms.maps.model.LatLng

@Composable
fun HomeRoute(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val location by viewModel.locationState.collectAsStateWithLifecycle()
    val incidentsState by viewModel.incidentsState.collectAsStateWithLifecycle()

    HomeScreen(
        location = location,
        incidentsState = incidentsState,
        onUpdateBoundingBox = viewModel::updateBoundingBoxState
    )
}

@Composable
private fun HomeScreen(
    location: LatLng,
    incidentsState: IncidentsState,
    onUpdateBoundingBox: (LatLng, LatLng) -> Unit
) {
    HomeBottomSheetScaffold(
        sheetContent = {
            IncidentContent(
                incidentsState = incidentsState
            )
        }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            HomeMapView(
                ratLng = location,
                onUpdateBoundingBox = onUpdateBoundingBox
            )
        }
    }
}

@Composable
private fun IncidentContent(
    incidentsState: IncidentsState
) {
    when (incidentsState) {
        IncidentsState.Loading -> {}
        is IncidentsState.Success -> {
            IncidentList(
                incidents = incidentsState.incidents,
                typeFilters = incidentsState.typeFilters,
                sort = incidentsState.selectedSort.sort.text,
                onSortClick = {},
                onFilterClick = {},
                onSearchClick = {}
            )
        }
    }
}