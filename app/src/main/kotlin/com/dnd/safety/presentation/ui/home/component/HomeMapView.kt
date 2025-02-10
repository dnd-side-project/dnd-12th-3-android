package com.dnd.safety.presentation.ui.home.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.dnd.safety.R
import com.dnd.safety.domain.model.Incidents
import com.dnd.safety.utils.Logger
import com.dnd.safety.utils.icon
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.ComposeMapColorScheme
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MarkerComposable
import com.google.maps.android.compose.MarkerState
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
                CustomMapMarker(
                    id = incident.incidentCategory.icon,
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

@Composable
fun MyLocationMarker(
    myLocation: LatLng?
) {
    if (myLocation == null) return

    CustomMapMarker(
        id = R.drawable.ic_location,
        fullName = "My Location",
        location = myLocation,
    )
}

@Composable
fun CustomMapMarker(
    id: Int,
    fullName: String = "",
    location: LatLng,
    onClick: () -> Unit = {}
) {
    val markerState = remember { MarkerState(position = location) }

    LaunchedEffect(location) {
        markerState.position = location
    }

    MarkerComposable(
        keys = arrayOf(fullName, location),
        state = markerState,
        title = fullName,
        anchor = Offset(0.5f, 1f),
        onClick = {
            onClick()
            true
        }
    ) {
        Image(
            painter = painterResource(id),
            contentDescription = "Profile Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(38.71.dp, 47.dp)
        )
    }
}