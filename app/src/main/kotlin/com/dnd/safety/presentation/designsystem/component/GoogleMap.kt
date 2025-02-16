package com.dnd.safety.presentation.designsystem.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.dnd.safety.R
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.GoogleMapComposable
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings

@Composable
fun MyGoogleMap(
    cameraPositionState: CameraPositionState,
    modifier: Modifier = Modifier,
    mapProperties: MapProperties = getMapDefaultProperties(),
    content: @Composable @GoogleMapComposable () -> Unit = {},
) {
    GoogleMap(
        modifier = modifier,
        cameraPositionState = cameraPositionState,
        properties = mapProperties,
        uiSettings = MapUiSettings(
            zoomControlsEnabled = false,
            compassEnabled = false,
            mapToolbarEnabled = false
        ),
        content = content
    )
}

@Composable
fun getMapDefaultProperties() = MapProperties(
    mapStyleOptions = MapStyleOptions.loadRawResourceStyle(LocalContext.current, R.raw.map_style),
    isBuildingEnabled = false,
    isIndoorEnabled = false,
    isMyLocationEnabled = false,
    isTrafficEnabled = false
)