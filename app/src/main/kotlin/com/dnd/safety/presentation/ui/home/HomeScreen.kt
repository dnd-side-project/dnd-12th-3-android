package com.dnd.safety.presentation.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dnd.safety.presentation.designsystem.component.BottomSheet
import com.dnd.safety.presentation.ui.home.component.HomeBottomSheetScaffold
import com.dnd.safety.presentation.ui.home.component.HomeMapView
import com.dnd.safety.presentation.ui.home.component.IncidentList
import com.dnd.safety.presentation.ui.home.component.SortSheet
import com.dnd.safety.presentation.ui.home.state.HomeModalState
import com.dnd.safety.presentation.ui.home.state.HomeUiState
import com.dnd.safety.presentation.ui.home.state.IncidentsState
import com.google.android.gms.maps.model.LatLng

@Composable
fun HomeRoute(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val location by viewModel.locationState.collectAsStateWithLifecycle()
    val incidentsState by viewModel.incidentsState.collectAsStateWithLifecycle()
    val homeUiState by viewModel.homeUiState.collectAsStateWithLifecycle()
    val modalState by viewModel.homeModalState.collectAsStateWithLifecycle()

    HomeScreen(
        incidentsState = incidentsState,
        homeUiState = homeUiState,
        location = location,
        viewModel = viewModel,
    )

    ModalContent(
        modalState = modalState,
        viewModel = viewModel
    )
}

@Composable
private fun HomeScreen(
    incidentsState: IncidentsState,
    homeUiState: HomeUiState,
    location: LatLng,
    viewModel: HomeViewModel,
) {
    HomeBottomSheetScaffold(
        sheetContent = {
            IncidentContent(
                incidentsState = incidentsState,
                homeUiState = homeUiState,
                viewModel = viewModel
            )
        }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            HomeMapView(
                ratLng = location,
                onUpdateBoundingBox = viewModel::updateBoundingBoxState
            )
        }
    }
}

@Composable
private fun IncidentContent(
    incidentsState: IncidentsState,
    homeUiState: HomeUiState,
    viewModel: HomeViewModel,
) {
    when (incidentsState) {
        IncidentsState.Loading -> {}
        is IncidentsState.Success -> {
            IncidentList(
                incidents = incidentsState.incidents,
                typeFilters = homeUiState.typeFilters,
                sort = homeUiState.selectedSort.sort.text,
                onSortClick = viewModel::showSortModal,
                onFilterClick = viewModel::setIncidentTypeFilter,
                onSearchClick = {}
            )
        }
    }
}

@Composable
fun HomeSearchBar(modifier: Modifier = Modifier) {

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ModalContent(
    modalState: HomeModalState,
    viewModel: HomeViewModel
) {
    when (modalState) {
        HomeModalState.Dismiss -> {}
        is HomeModalState.ShowSortSheet -> {
            SortSheet(
                sortFilters = modalState.sortFilters,
                onSelectSort = viewModel::setSortFilter,
                onDismissRequest = viewModel::dismissModal
            )
        }
    }
}