package com.dnd.safety.presentation.ui.locationconfirm

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.dnd.safety.data.model.Location
import com.dnd.safety.presentation.navigation.Route
import com.dnd.safety.presentation.navigation.utils.toRouteType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class LocationConfirmViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    val nickname = savedStateHandle.get<String>(Route.LocationConfirm.argNickname) ?: ""
    val locationString = savedStateHandle.get<String>(Route.LocationConfirm.argLocation) ?: ""
    val location = Json.decodeFromString<Location>(locationString)

    init {
        println("LocationConfirmViewModel - nickname: $nickname")
        println("LocationConfirmViewModel - location: $location")
        println("LocationConfirmViewModel - location.placeName: ${location.placeName}")
        println("LocationConfirmViewModel - location.address: ${location.address}")
    }

    private val _state = MutableStateFlow(LocationConfirmState())
    val state: StateFlow<LocationConfirmState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<LocationConfirmEffect>()
    val effect = _effect.asSharedFlow()

    fun requestLocationPermission(permissionGranted: Boolean) {
        _state.value = _state.value.copy(isPermissionGranted = permissionGranted)
        if (!permissionGranted) {
            viewModelScope.launch {
                _effect.emit(LocationConfirmEffect.ShowPermissionDeniedMessage)
            }
        }
    }

    fun requestNotificationPermission(permissionGranted: Boolean) {
        _state.value = _state.value.copy(isNotificationPermissionGranted = permissionGranted)
        if (!permissionGranted) {
            viewModelScope.launch {
                _effect.emit(LocationConfirmEffect.ShowPermissionDeniedMessage)
            }
        }
    }

    fun onNextButtonClicked() {
        viewModelScope.launch {
            _effect.emit(LocationConfirmEffect.NavigateToMainScreen)
        }
    }
}