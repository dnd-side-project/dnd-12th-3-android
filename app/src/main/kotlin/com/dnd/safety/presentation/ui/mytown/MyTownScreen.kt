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
import com.dnd.safety.presentation.designsystem.theme.SafetyTheme
import com.dnd.safety.presentation.ui.home.component.MyLocationMarker
import com.dnd.safety.presentation.ui.mytown.component.MyTownSheet
import com.dnd.safety.presentation.ui.mytown.effect.MyTownModalState
import com.dnd.safety.presentation.ui.mytown.state.MyTownUiState
import com.dnd.safety.presentation.ui.search_dialog.SearchDialog
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
                myLocation = myLocation,
                firstMyTown = myTownUiState.firstMyTown,
                secondMyTown = myTownUiState.secondMyTown,
                onAddClick = viewModel::showTownSearch,
                onDeleteClick = viewModel::deleteMyTown,
                onSelectClick = viewModel::selectMyTown
            )
        }
    }
}

@Composable
private fun MyTownScreen(
    myLocation: LatLng,
    firstMyTown: MyTown?,
    secondMyTown: MyTown?,
    onAddClick: () -> Unit,
    onDeleteClick: (MyTown) -> Unit,
    onSelectClick: (MyTown) -> Unit,
) {
    val cameraPositionState = rememberCameraPositionState {
        CameraPosition.fromLatLngZoom(myLocation, 15f)
    }

    Scaffold { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
        ) {
            MyGoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState
            ) {
                MyLocationMarker(myLocation)
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

    LaunchedEffect(myLocation) {
        val currentZoom = cameraPositionState.position.zoom.takeIf { it > 6f } ?: 15f
        cameraPositionState.animate(CameraUpdateFactory.newLatLngZoom(myLocation, currentZoom))
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
            SearchDialog(
                onPlaceSelected = { latLng, placeName ->
                    viewModel.selectMyTown(MyTown(1, placeName, true))
                },
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
            firstMyTown = MyTown(1, "My Town", true),
            secondMyTown = null,
            onAddClick = {},
            onDeleteClick = {},
            onSelectClick = {},
            myLocation = LatLng(37.5665, 126.9780)
        )
    }
}