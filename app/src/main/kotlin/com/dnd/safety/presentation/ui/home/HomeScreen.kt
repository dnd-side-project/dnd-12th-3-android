package com.dnd.safety.presentation.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationDisabled
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dnd.safety.presentation.designsystem.theme.Gray60
import com.dnd.safety.presentation.designsystem.theme.Gray80
import com.dnd.safety.presentation.designsystem.theme.SafetyTheme
import com.dnd.safety.presentation.designsystem.theme.White
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
    val cameraLocationState by viewModel.cameraLocationState.collectAsStateWithLifecycle()
    val myLocation by viewModel.myLocation.collectAsStateWithLifecycle()
    val incidentsState by viewModel.incidentsState.collectAsStateWithLifecycle()
    val homeUiState by viewModel.homeUiState.collectAsStateWithLifecycle()
    val modalState by viewModel.homeModalState.collectAsStateWithLifecycle()

    HomeScreen(
        incidentsState = incidentsState,
        homeUiState = homeUiState,
        myLocation = myLocation,
        cameraLocation = cameraLocationState,
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
            modifier = Modifier.fillMaxSize()
        ) {
            HomeMapView(
                myLocation = myLocation,
                cameraLocation = cameraLocation,
                incidents = if (incidentsState is IncidentsState.Success) incidentsState.incidents else emptyList(),
                onUpdateBoundingBox = viewModel::updateBoundingBoxState,
                onUpdateLocation = viewModel::setSearchPlace,
            )

            HomeSearchBar(
                keyword = homeUiState.keyword,
                onShowSearchDialog = viewModel::showSearchModal,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(16.dp)
            )

            FloatingActionButton(
                onClick = viewModel::setIsCurrentLocation,
                modifier = Modifier
                    .padding(it)
                    .align(Alignment.BottomEnd)
                    .padding(16.dp),
                containerColor = Gray60,
                contentColor = White
            ) {
                Icon(
                    imageVector = if (homeUiState.isCurrentLocation) Icons.Default.MyLocation else Icons.Default.LocationDisabled,
                    contentDescription = "내 위치로 이동"
                )
            }
        }
    }
}

@Composable
private fun HomeSearchBar(
    keyword: String,
    onShowSearchDialog: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(60.dp))
            .background(
                Gray80,
                RoundedCornerShape(60.dp)
            )
            .clickable(onClick = onShowSearchDialog)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(
                vertical = 10.dp, horizontal = 10.dp
            )
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                modifier = Modifier.size(30.dp)
            )
        }
        Text(
            text = keyword,
            style = SafetyTheme.typography.paragraph2,
            modifier = Modifier.align(Alignment.Center)
        )
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
        HomeModalState.ShowSearchDialog -> {
            SearchDialog(
                onPlaceSelected = viewModel::setSearchPlace,
                onDismissRequest = viewModel::dismissModal,
            )
        }
    }
}


