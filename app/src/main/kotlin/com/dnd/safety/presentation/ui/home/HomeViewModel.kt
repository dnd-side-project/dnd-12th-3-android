package com.dnd.safety.presentation.ui.home

import androidx.lifecycle.ViewModel
import com.dnd.safety.location.LocationService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    val locationService: LocationService
) : ViewModel() {


}