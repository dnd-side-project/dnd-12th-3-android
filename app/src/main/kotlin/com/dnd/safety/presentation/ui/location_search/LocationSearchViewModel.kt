package com.dnd.safety.presentation.ui.location_search

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dnd.safety.data.model.Location
import com.dnd.safety.domain.usecase.FetchLocationUseCase
import com.dnd.safety.presentation.navigation.Route
import com.dnd.safety.presentation.navigation.utils.toRouteType
import com.dnd.safety.presentation.ui.location_search.effect.LocationSearchEffect
import com.dnd.safety.presentation.ui.location_search.state.LocationSearchState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationSearchViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val fetchLocationUseCase: FetchLocationUseCase,
) : ViewModel() {
    private val searchLocation = savedStateHandle.toRouteType<Route.SearchLocation, Location>()
    val nickname = searchLocation.nickname

    private val _state = MutableStateFlow(LocationSearchState())
    val state = _state.asStateFlow()

    private val _effect = Channel<LocationSearchEffect>()
    val effect = _effect.receiveAsFlow()

    init {
        Log.d("LocationSearchViewModel", "nickname: $nickname")
    }

    fun updateSearchQuery(query: String) {
        _state.update { it.copy(searchQuery = query) }
    }

    fun onLocationSelect(location: Location) {
        viewModelScope.launch {
            _effect.send(LocationSearchEffect.NavigateToLocationConfirm(location))
        }
    }

    fun searchLocation() = viewModelScope.launch {
        _state.update { it.copy(isLoading = true) }

        fetchLocationUseCase(state.value.searchQuery)
            .onSuccess { locations ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        locations = locations,
                        hasSearched = true,
                    )
                }
            }
            .onFailure { error ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        locations = emptyList(),
                        hasSearched = true,
                    )
                }
                _effect.send(
                    LocationSearchEffect.ShowToast(
                        error.message ?: "검색 중 오류가 발생했습니다"
                    )
                )
            }
    }
}