package com.dnd.safety.presentation.ui.location_search

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.dnd.safety.data.model.Location
import com.dnd.safety.presentation.navigation.Route
import com.dnd.safety.presentation.navigation.utils.toRouteType
import com.dnd.safety.presentation.ui.location_search.effect.LocationConfirmEffect
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


    private val _effect = MutableSharedFlow<LocationConfirmEffect>()
    val effect = _effect.asSharedFlow()

    fun onNextButtonClicked() {
        viewModelScope.launch {
            _effect.emit(LocationConfirmEffect.NavigateToMainScreen)
        }
    }
}