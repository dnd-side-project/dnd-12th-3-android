package com.dnd.safety.presentation.ui.home.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.dnd.safety.R
import com.dnd.safety.domain.model.Incidents
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
    ratLng: LatLng,
    incidents: List<Incidents>,
    onUpdateBoundingBox: (LatLng, LatLng) -> Unit,
    onUpdateLocation: (LatLng) -> Unit,
    modifier: Modifier = Modifier
) {
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(ratLng, 10f)
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
            MyLocationMarker(
                ratLng
            )

            incidents.forEach { incident ->
                CustomMapMarker(
                    id = incident.incidentCategory.icon,
                    location = LatLng(incident.pointX, incident.pointY),
                )
            }
        }
    }

    LaunchedEffect(ratLng) {
        cameraPositionState.animate(CameraUpdateFactory.newLatLngZoom(ratLng, 15f))
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
    ratLng: LatLng
) {
    val markerState = remember { MarkerState(position = ratLng) }

    LaunchedEffect(ratLng) {
        markerState.position = ratLng
    }

    CustomMapMarker(
        id = R.drawable.ic_location,
        fullName = "My Location",
        location = ratLng,
        markerState = markerState
    )
}


@Composable
fun CustomMapMarker(
    id: Int,
    fullName: String = "",
    location: LatLng,
    markerState: MarkerState = remember { MarkerState(position = location) },
    onClick: () -> Unit = {}
) {
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