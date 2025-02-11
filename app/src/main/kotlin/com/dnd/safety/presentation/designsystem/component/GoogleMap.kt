package com.dnd.safety.presentation.designsystem.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.GoogleMapComposable
import com.google.maps.android.compose.MapUiSettings

@Composable
fun MyGoogleMap(
    cameraPositionState: CameraPositionState,
    modifier: Modifier = Modifier,
    content: @Composable @GoogleMapComposable () -> Unit = {},
) {
    GoogleMap(
        modifier = modifier,
        cameraPositionState = cameraPositionState,
        uiSettings = MapUiSettings(
            zoomControlsEnabled = false,
            compassEnabled = false,
            mapToolbarEnabled = false
        ),
        content = content
    )
}