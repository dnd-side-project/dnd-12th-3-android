package com.dnd.safety.presentation.ui.home.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.dnd.safety.domain.model.Incident
import com.dnd.safety.presentation.designsystem.component.MyGoogleMap
import com.dnd.safety.utils.icon
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState

@Composable
fun HomeMapView(
    cameraPositionState: CameraPositionState,
    myLocation: LatLng?,
    incidents: List<Incident>,
    onUpdateBoundingBox: (LatLng, LatLng) -> Unit,
    modifier: Modifier = Modifier
) {
    HomeMapViewContent(
        cameraPositionState = cameraPositionState,
        myLocation = myLocation,
        incidents = incidents,
        onUpdateBoundingBox = onUpdateBoundingBox,
        modifier = modifier
    )
}

@Composable
fun HomeMapViewContent(
    cameraPositionState: CameraPositionState,
    myLocation: LatLng?,
    incidents: List<Incident>,
    onUpdateBoundingBox: (LatLng, LatLng) -> Unit,
    modifier: Modifier = Modifier
) {
    val markerList = remember { mutableStateListOf<Incident>() }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        MyGoogleMap(
            modifier = modifier.fillMaxSize(),
            onMapLoaded = {
                cameraPositionState.projection?.visibleRegion?.latLngBounds?.let { bounds ->
                    onUpdateBoundingBox(bounds.northeast, bounds.southwest)
                }
            },
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