package com.dnd.safety.presentation.ui.location_search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dnd.safety.presentation.designsystem.component.MyGoogleMap
import com.dnd.safety.presentation.designsystem.component.TopAppbar
import com.dnd.safety.presentation.designsystem.component.WatchOutButton
import com.dnd.safety.presentation.designsystem.component.getMapDefaultProperties
import com.dnd.safety.presentation.designsystem.theme.Gray60
import com.dnd.safety.presentation.designsystem.theme.SafetyTheme
import com.dnd.safety.presentation.ui.home.component.MyLocationMarker
import com.dnd.safety.presentation.ui.location_search.effect.LocationConfirmEffect
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.flow.collectLatest

@Composable
fun LocationConfirmScreen(
    address: String,
    location: LatLng,
    onGoBack: () -> Unit,
    onComplete: () -> Unit,
    onShowSnackBar: (String) -> Unit,
    viewModel: LocationConfirmViewModel = hiltViewModel(),
) {
    val cameraPositionState = rememberCameraPositionState {
        CameraPosition.fromLatLngZoom(location, 15f)
    }

    Scaffold(
        topBar = {
            TopAppbar(
                onBackEvent = onGoBack,
            )
        },
        bottomBar = {
            WatchOutButton(
                text = "확인",
                onClick = viewModel::addMyTown,
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 20.dp)
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding(),
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(100.dp))
            Text(
                text = "해당 주소로 알림을\n보내드릴까요?",
                style = SafetyTheme.typography.title1,
                modifier = Modifier.padding(horizontal = 4.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Surface(
                shape = RoundedCornerShape(15.dp),
                modifier = Modifier
            ) {
                MyGoogleMap(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    cameraPositionState = cameraPositionState,
                    mapProperties = getMapDefaultProperties().copy(
                        minZoomPreference = 15f,
                        maxZoomPreference = 15f,
                        latLngBoundsForCameraTarget = LatLngBounds(
                            location, location
                        )
                    )
                ) {
                    MyLocationMarker(location)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = address,
                style = SafetyTheme.typography.paragraph2,
                color = Gray60,
                modifier = Modifier.padding(horizontal = 4.dp)
            )
        }
    }

    LaunchedEffect(location) {
        val currentZoom = cameraPositionState.position.zoom.takeIf { it > 6f } ?: 15f
        cameraPositionState.animate(CameraUpdateFactory.newLatLngZoom(location, currentZoom))
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest {
            when (it) {
                is LocationConfirmEffect.NavigateToMainScreen -> onComplete()
                is LocationConfirmEffect.ShowSnackBar -> onShowSnackBar(it.message)
            }
        }
    }
}