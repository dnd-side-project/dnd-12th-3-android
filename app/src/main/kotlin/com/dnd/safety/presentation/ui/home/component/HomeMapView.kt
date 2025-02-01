package com.dnd.safety.presentation.ui.home.component

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.dnd.safety.utils.Logger
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun HomeMapView(
    ratLng: LatLng,
    onUpdateBoundingBox: (LatLng, LatLng) -> Unit,
    modifier: Modifier = Modifier
) {
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(ratLng, 10f)
    }

    GoogleMap(
        modifier = modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    )

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
            }
        }
    }
}