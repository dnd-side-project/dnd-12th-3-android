package com.dnd.safety.presentation.ui.mytown

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dnd.safety.domain.model.MyTown
import com.dnd.safety.presentation.designsystem.component.FadeAnimatedVisibility
import com.dnd.safety.presentation.designsystem.component.MyGoogleMap
import com.dnd.safety.presentation.designsystem.component.TopAppbar
import com.dnd.safety.presentation.designsystem.theme.SafetyTheme
import com.dnd.safety.presentation.ui.home.component.MyLocationMarker
import com.dnd.safety.presentation.ui.mytown.component.MyTownSheet
import com.dnd.safety.presentation.ui.mytown.effect.MyTownModalState
import com.dnd.safety.presentation.ui.mytown.state.MyTownUiState
import com.dnd.safety.presentation.ui.search_address_dialog.SearchAddressDialog
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun MyTownRoute(
    viewModel: MyTownViewModel = hiltViewModel()
) {
    val myLocation by viewModel.myLocation.collectAsStateWithLifecycle()
    val myTownUiState by viewModel.myTownUiState.collectAsStateWithLifecycle()
    val modalState by viewModel.myTownModalState.collectAsStateWithLifecycle()

    MyTownContent(
        myTownUiState = myTownUiState,
        myLocation = myLocation,
        viewModel = viewModel
    )

    MyTownModelContent(
        modalState = modalState,
        viewModel = viewModel
    )
}

@Composable
fun MyTownContent(
    myTownUiState: MyTownUiState,
    myLocation: LatLng,
    viewModel: MyTownViewModel
) {
    FadeAnimatedVisibility(
        myTownUiState is MyTownUiState.Success
    ) {
        if (myTownUiState is MyTownUiState.Success) {
            MyTownScreen(
                location = myTownUiState.selectedLocation ?: myLocation,
                firstMyTown = myTownUiState.firstMyTown,
                secondMyTown = myTownUiState.secondMyTown,
                onAddClick = viewModel::showTownSearch,
                onDeleteClick = viewModel::deleteMyTown,
                onSelectClick = viewModel::selectTown
            )
        }
    }
}

@Composable
private fun MyTownScreen(
    location: LatLng,
    firstMyTown: MyTown?,
    secondMyTown: MyTown?,
    onAddClick: () -> Unit,
    onDeleteClick: (MyTown) -> Unit,
    onSelectClick: (MyTown) -> Unit,
) {
    val cameraPositionState = rememberCameraPositionState {
        CameraPosition.fromLatLngZoom(location, 15f)
    }

    Scaffold(
        topBar = {
            TopAppbar(
                title = "마이페이지",
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
        ) {
            MyGoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState
            ) {
                MyLocationMarker(location)
            }

            MyTownSheet(
                firstMyTown = firstMyTown,
                secondMyTown = secondMyTown,
                onAddClick = onAddClick,
                onDeleteClick = onDeleteClick,
                onSelectClick = onSelectClick,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }

    LaunchedEffect(location) {
        val currentZoom = cameraPositionState.position.zoom.takeIf { it > 6f } ?: 15f
        cameraPositionState.animate(CameraUpdateFactory.newLatLngZoom(location, currentZoom))
    }
}

@Composable
fun MyTownModelContent(
    modalState: MyTownModalState,
    viewModel: MyTownViewModel
) {
    when (modalState) {
        MyTownModalState.Dismiss -> {}
        MyTownModalState.ShowSearchDialog -> {
            SearchAddressDialog(
                onAddressSelected = viewModel::addressSelected,
                onDismissRequest = viewModel::dismissModal,
            )
        }
    }
}

@Preview
@Composable
private fun MyTownScreenPreview() {
    SafetyTheme {
        MyTownScreen(
            firstMyTown = MyTown(1, "My Town", LatLng(37.5665, 126.9780), true),
            secondMyTown = null,
            onAddClick = {},
            onSelectClick = {},
            onDeleteClick = {},
            location = LatLng(37.5665, 126.9780)
        )
    }
}