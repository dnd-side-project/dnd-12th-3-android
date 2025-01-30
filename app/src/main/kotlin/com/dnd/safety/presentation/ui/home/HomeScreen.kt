package com.dnd.safety.presentation.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dnd.safety.presentation.ui.home.component.HomeMapView
import com.google.android.gms.maps.model.LatLng

@Composable
fun HomeRoute(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val location by viewModel.location.collectAsStateWithLifecycle()

    HomeScreen(
        location = location ?: LatLng(0.0, 0.0)
    )
}

@Composable
fun HomeScreen(
    location: LatLng
) {
    Scaffold {
        Box(
            modifier = Modifier
                .padding(it)
        ) {
            HomeMapView(
                ratLng = location
            )
        }
    }
}
