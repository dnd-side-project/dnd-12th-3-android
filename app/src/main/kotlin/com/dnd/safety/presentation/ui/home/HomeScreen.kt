@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.dnd.safety.presentation.ui.home

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dnd.safety.domain.model.Incident
import com.dnd.safety.presentation.designsystem.component.BottomToTopAnimatedVisibility
import com.dnd.safety.presentation.designsystem.component.circleBackground
import com.dnd.safety.presentation.designsystem.theme.Gray10
import com.dnd.safety.presentation.designsystem.theme.Gray70
import com.dnd.safety.presentation.designsystem.theme.SafetyTheme
import com.dnd.safety.presentation.designsystem.theme.White
import com.dnd.safety.presentation.navigation.MainTab
import com.dnd.safety.presentation.ui.home.component.HomeBottomBar
import com.dnd.safety.presentation.ui.home.component.HomeBottomSheetScaffold
import com.dnd.safety.presentation.ui.home.component.HomeMapView
import com.dnd.safety.presentation.ui.home.component.HomeSearchBar
import com.dnd.safety.presentation.ui.home.component.IncidentList
import com.dnd.safety.presentation.ui.home.effect.HomeUiEffect
import com.dnd.safety.presentation.ui.home.state.HomeModalState
import com.dnd.safety.presentation.ui.home.state.HomeUiState
import com.dnd.safety.presentation.ui.home.state.IncidentsState
import com.dnd.safety.presentation.ui.search_dialog.SearchDialog
import com.dnd.safety.utils.Const.SEOUL_LAT_LNG
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.collectLatest

@Composable
fun HomeRoute(
    onIncidentDetail: (Incident) -> Unit,
    onBottomNavClicked: (MainTab) -> Unit,
    onShowSnackBar: (String) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val myLocation by viewModel.myLocation.collectAsStateWithLifecycle()
    val incidentsState by viewModel.incidentsState.collectAsStateWithLifecycle()
    val homeUiState by viewModel.homeUiState.collectAsStateWithLifecycle()
    val modalState by viewModel.homeModalState.collectAsStateWithLifecycle()
    val keyword by viewModel.keyword.collectAsStateWithLifecycle()

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(myLocation ?: SEOUL_LAT_LNG, 15f)
    }

    HomeScreen(
        cameraPositionState = cameraPositionState,
        incidentsState = incidentsState,
        homeUiState = homeUiState,
        myLocation = myLocation,
        keyword = keyword,
        viewModel = viewModel,
        onBottomNavClicked = onBottomNavClicked
    )

    ModalContent(
        modalState = modalState,
        viewModel = viewModel
    )

    LaunchedEffect(Unit) {
        viewModel.homeUiEffect.collectLatest {
            when (it) {
                is HomeUiEffect.ShowIncidentDetail -> onIncidentDetail(it.incident)
                is HomeUiEffect.MoveCameraToLocation -> {
                    val targetZoom = cameraPositionState.position.zoom.coerceAtMost(15f) // 최대 15로 제한
                    cameraPositionState.animate(CameraUpdateFactory.newLatLngZoom(it.latLng, targetZoom))
                }
                is HomeUiEffect.ShowSnackBar -> onShowSnackBar(it.message)
            }
        }
    }
}

@Composable
private fun HomeScreen(
    cameraPositionState: CameraPositionState,
    incidentsState: IncidentsState,
    homeUiState: HomeUiState,
    myLocation: LatLng?,
    keyword: String,
    viewModel: HomeViewModel,
    onBottomNavClicked: (MainTab) -> Unit,
) {
    Scaffold(
        bottomBar = {
            HomeBottomBar(
                bottomItems = MainTab.entries.toPersistentList(),
                onBottomItemClicked = onBottomNavClicked
            )
        },
        modifier = Modifier
    ) { paddingValues ->
        HomeBottomSheetScaffold(
            sheetContent = {
                Box{
                    IncidentContent(
                        incidentsState = incidentsState,
                        homeUiState = homeUiState,
                        viewModel = viewModel,
                    )
                }
            },
        ) {
            val padding = PaddingValues(
                start = (it.calculateLeftPadding(LayoutDirection.Ltr) - paddingValues.calculateLeftPadding(LayoutDirection.Ltr)).coerceAtLeast(0.dp),
                top = (it.calculateTopPadding() - paddingValues.calculateTopPadding()).coerceAtLeast(0.dp),
                end = (it.calculateRightPadding(LayoutDirection.Ltr) - paddingValues.calculateRightPadding(LayoutDirection.Ltr)).coerceAtLeast(0.dp),
                bottom = (it.calculateBottomPadding() - paddingValues.calculateBottomPadding()).coerceAtLeast(0.dp)
            )
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                HomeMapView(
                    cameraPositionState = cameraPositionState,
                    myLocation = myLocation,
                    incidents = if (incidentsState is IncidentsState.Success) incidentsState.incidents else emptyList(),
                    onUpdateBoundingBox = viewModel::updateBoundingBoxState,
                    onShowIncidentDetail = viewModel::showIncidentDetail,
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
                        .padding(padding)
                        .padding(bottom = 40.dp)
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

                BottomToTopAnimatedVisibility(
                    visible = incidentsState is IncidentsState.Success && incidentsState.incidents.isEmpty(),
                    modifier = Modifier
                        .padding(padding)
                        .padding(bottom = 40.dp)
                        .align(Alignment.BottomCenter)
                ) {
                    Surface(
                        color = White,
                        shadowElevation = 8.dp,
                        border = BorderStroke(0.7.dp, Gray10),
                        shape = RoundedCornerShape(25.dp),
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "지도에 표시할 사고가 없습니다",
                            style = SafetyTheme.typography.label1,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }

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
                onShowDetail = viewModel::showIncidentDetail,
                onLike = viewModel::likeIncident,
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


