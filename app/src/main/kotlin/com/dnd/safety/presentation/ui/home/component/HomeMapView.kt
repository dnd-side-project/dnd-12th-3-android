package com.dnd.safety.presentation.ui.home.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.dnd.safety.domain.model.Incidents
import com.dnd.safety.utils.icon
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.ComposeMapColorScheme
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun HomeMapView(
    myLocation: LatLng?,
    cameraLocation: LatLng,
    incidents: List<Incidents>,
    onUpdateBoundingBox: (LatLng, LatLng) -> Unit,
    onUpdateLocation: (LatLng) -> Unit,
    modifier: Modifier = Modifier
) {
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(cameraLocation, 15f)
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        GoogleMap(
            modifier = modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            mapColorScheme = ComposeMapColorScheme.DARK,
            uiSettings = MapUiSettings(
                zoomControlsEnabled = false,
                compassEnabled = false,
                mapToolbarEnabled = false
            )
        ) {
            MyLocationMarker(myLocation)

            incidents.forEach { incident ->
                IncidentsMarker(
                    iconId = incident.incidentCategory.icon,
                    location = LatLng(incident.pointY, incident.pointX),
                )
            }
        }
    }

    LaunchedEffect(cameraLocation) {
        val currentZoom = cameraPositionState.position.zoom.takeIf { it > 6f } ?: 15f
        cameraPositionState.animate(CameraUpdateFactory.newLatLngZoom(cameraLocation, currentZoom))
    }

    LaunchedEffect(cameraPositionState.isMoving) {
        if (!cameraPositionState.isMoving) {
            val projection = cameraPositionState.projection
            if (projection != null) {
                val bounds = projection.visibleRegion.latLngBounds
                val northeastLatLng = bounds.northeast
                val southwestLatLng = bounds.southwest
                onUpdateBoundingBox(northeastLatLng, southwestLatLng)

                val center = cameraPositionState.position.target
                onUpdateLocation(center) // 카메라 중심 업데이트
            }
        }
    }
}

