package com.dnd.safety.presentation.ui.home.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.dnd.safety.domain.model.Incidents
import com.dnd.safety.presentation.designsystem.component.MyGoogleMap
import com.dnd.safety.presentation.designsystem.component.ProgressIndicator
import com.dnd.safety.utils.icon
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun HomeMapView(
    myLocation: LatLng?,
    cameraLocation: LatLng?,
    incidents: List<Incidents>,
    onUpdateBoundingBox: (LatLng, LatLng) -> Unit,
    modifier: Modifier = Modifier
) {
    if (cameraLocation == null) {
        ProgressIndicator()
    } else {
        HomeMapViewContent(
            myLocation = myLocation,
            cameraLocation = cameraLocation,
            incidents = incidents,
            onUpdateBoundingBox = onUpdateBoundingBox,
            modifier = modifier
        )
    }
}

@Composable
fun HomeMapViewContent(
    myLocation: LatLng?,
    cameraLocation: LatLng,
    incidents: List<Incidents>,
    onUpdateBoundingBox: (LatLng, LatLng) -> Unit,
    modifier: Modifier = Modifier
) {
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(cameraLocation, 15f)
    }
    val markerList = remember { mutableStateListOf<Incidents>() }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        MyGoogleMap(
            modifier = modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState
        ) {
            MyLocationMarker(myLocation)

            markerList.forEach { data ->
                IncidentsMarker(
                    iconId = data.incidentCategory.icon,
                    location = LatLng(data.pointY, data.pointX),
                )
            }
        }
    }

    LaunchedEffect(cameraLocation) {
        val targetZoom = cameraPositionState.position.zoom.coerceAtMost(15f) // 최대 15로 제한
        cameraPositionState.animate(CameraUpdateFactory.newLatLngZoom(cameraLocation, targetZoom))
    }

    LaunchedEffect(cameraPositionState.isMoving) {
        if (!cameraPositionState.isMoving) {
            cameraPositionState.projection?.visibleRegion?.latLngBounds?.let { bounds ->
                onUpdateBoundingBox(bounds.northeast, bounds.southwest)
            }
        }
    }

    LaunchedEffect(incidents) {
        markerList.clear()
        markerList.addAll(incidents)
    }
}