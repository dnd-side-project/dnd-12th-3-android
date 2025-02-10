package com.dnd.safety.presentation.ui.home.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dnd.safety.R
import com.dnd.safety.presentation.designsystem.theme.SafetyTheme
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.MarkerComposable
import com.google.maps.android.compose.MarkerState

@Composable
fun MyLocationMarker(
    myLocation: LatLng?
) {
    if (myLocation == null) return

    CustomMapMarker(
        fullName = "My Location",
        location = myLocation,
    ) {
        Image(
            painter = painterResource(R.drawable.ic_location),
            contentDescription = "Profile Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(38.71.dp, 47.dp)
        )
    }
}

@Composable
fun IncidentsMarker(
    iconId: Int,
    location: LatLng,
) {
    CustomMapMarker(
        fullName = "My Location",
        location = location,
    ) {
        Box {
            Image(
                painter = painterResource(R.drawable.ic_location),
                contentDescription = "Profile Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(38.71.dp, 47.dp)
            )
            Column(
                modifier = Modifier
            ) {
                Spacer(modifier = Modifier.height(4.dp))
                Image(
                    painter = painterResource(iconId),
                    contentDescription = "Profile Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .padding(horizontal = 5.dp)
                        .size(30.dp)
                )
            }
        }
    }
}

@Composable
private fun CustomMapMarker(
    location: LatLng,
    fullName: String = "",
    onClick: () -> Unit = {},
    content: @Composable () -> Unit
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
        },
        content = content
    )
}

@Preview
@Composable
private fun IncidentsMarkerPreview() {
    SafetyTheme {
        IncidentsMarker(
            iconId = R.drawable.ic_fire,
            location = LatLng(0.0, 0.0)
        )
    }
}