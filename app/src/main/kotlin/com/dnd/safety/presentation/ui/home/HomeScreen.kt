package com.dnd.safety.presentation.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dnd.safety.presentation.designsystem.component.circleBackground
import com.dnd.safety.presentation.designsystem.theme.Gray70
import com.dnd.safety.presentation.designsystem.theme.White
import com.dnd.safety.presentation.ui.home.component.HomeBottomSheetScaffold
import com.dnd.safety.presentation.ui.home.component.HomeMapView
import com.dnd.safety.presentation.ui.home.component.HomeSearchBar
import com.dnd.safety.presentation.ui.home.component.IncidentList
import com.dnd.safety.presentation.ui.home.state.HomeModalState
import com.dnd.safety.presentation.ui.home.state.HomeUiState
import com.dnd.safety.presentation.ui.home.state.IncidentsState
import com.dnd.safety.presentation.ui.search_dialog.SearchDialog
import com.google.android.gms.maps.model.LatLng

@Composable
fun HomeRoute(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val cameraLocationState by viewModel.cameraLocationState.collectAsStateWithLifecycle()
    val myLocation by viewModel.myLocation.collectAsStateWithLifecycle()
    val incidentsState by viewModel.incidentsState.collectAsStateWithLifecycle()
    val homeUiState by viewModel.homeUiState.collectAsStateWithLifecycle()
    val modalState by viewModel.homeModalState.collectAsStateWithLifecycle()
    val keyword by viewModel.keyword.collectAsStateWithLifecycle()

    HomeScreen(
        incidentsState = incidentsState,
        homeUiState = homeUiState,
        myLocation = myLocation,
        cameraLocation = cameraLocationState,
        keyword = keyword,
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
    myLocation: LatLng?,
    cameraLocation: LatLng,
    keyword: String,
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
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            HomeMapView(
                myLocation = myLocation,
                cameraLocation = cameraLocation,
                incidents = if (incidentsState is IncidentsState.Success) incidentsState.incidents else emptyList(),
                onUpdateBoundingBox = viewModel::updateBoundingBoxState,
            )
            HomeSearchBar(
                keyword = keyword,
                onShowSearchDialog = viewModel::showSearchModal,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(16.dp)
            )
            Surface(
                onClick = viewModel::setLocationCurrent,
                shape = CircleShape,
                modifier = Modifier
                    .padding(it)
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.MyLocation,
                    contentDescription = "내 위치로 이동",
                    tint = Gray70,
                    modifier = Modifier
                        .padding(9.dp)
                        .circleBackground(White)
                )
            }
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
                onFilterClick = viewModel::setIncidentTypeFilter,
            )
        }
    }
}

@Composable
private fun ModalContent(
    modalState: HomeModalState,
    viewModel: HomeViewModel
) {
    when (modalState) {
        HomeModalState.Dismiss -> {}
        HomeModalState.ShowSearchDialog -> {
            SearchDialog(
                onPlaceSelected = viewModel::setSearchPlace,
                onDismissRequest = viewModel::dismissModal,
            )
        }
    }
}


