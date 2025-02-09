package com.dnd.safety.presentation.ui.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dnd.safety.domain.model.BoundingBox
import com.dnd.safety.domain.model.IncidentTypeFilter
import com.dnd.safety.domain.model.Point
import com.dnd.safety.domain.model.SortFilter
import com.dnd.safety.domain.repository.IncidentsRepository
import com.dnd.safety.location.LocationService
import com.dnd.safety.presentation.ui.home.state.BoundingBoxState
import com.dnd.safety.presentation.ui.home.state.HomeModalState
import com.dnd.safety.presentation.ui.home.state.HomeUiState
import com.dnd.safety.presentation.ui.home.state.IncidentsState
import com.google.android.gms.maps.model.LatLng
import com.skydoves.sandwich.ApiResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val locationService: LocationService,
    incidentsRepository: IncidentsRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _locationState = MutableStateFlow(SEOUL_LAT_LNG)
    val locationState: StateFlow<LatLng> get() = _locationState

    private val boundingBoxState = MutableStateFlow<BoundingBoxState>(BoundingBoxState.NotInitialized)

    private val _homeUiState = MutableStateFlow(HomeUiState())
    val homeUiState: StateFlow<HomeUiState> get() = _homeUiState

    val incidentsState: StateFlow<IncidentsState> = locationState.map { location ->
        when (val incidents = incidentsRepository.getIncidents(location)) {
            is ApiResponse.Success -> IncidentsState.Success(incidents.data)
            else -> IncidentsState.Loading
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(3000L),
        initialValue = IncidentsState.Loading
    )

    private val _homeModalState = MutableStateFlow<HomeModalState>(HomeModalState.Dismiss)
    val homeModalState: StateFlow<HomeModalState> get() = _homeModalState

    init {
        updateLocationByCurrent()
    }

    private fun updateLocationByCurrent() {
        viewModelScope.launch {
            locationService.requestLocationUpdates().collectLatest { location ->
                if (homeUiState.value.isCurrentLocation) {
                    _locationState.update { location ?: SEOUL_LAT_LNG }
                }
            }
        }
    }

    private fun updateLocationBySearch(location: LatLng) {
        viewModelScope.launch {
            _locationState.update { location }
        }
    }

    fun updateBoundingBoxState(
        topRight: LatLng,
        bottomLeft: LatLng
    ) {
        boundingBoxState.update {
            BoundingBoxState.Success(
                BoundingBox(
                    topRight = Point(topRight),
                    bottomLeft = Point(bottomLeft)
                )
            )
        }
    }

    fun setIncidentTypeFilter(type: IncidentTypeFilter) {
        _homeUiState.update {
            it.copy(
                typeFilters = it.typeFilters.map { filter ->
                    filter.copy(isSelected = filter == type)
                }
            )
        }
    }

    fun setSortFilter(sort: SortFilter) {
        _homeUiState.update {
            it.copy(
                sortFilters = it.sortFilters.map { filter ->
                    filter.copy(isSelected = filter == sort)
                }
            )
        }
    }

    fun setSearchPlace(latLng: LatLng, placeName: String = "") {
        updateLocationBySearch(latLng)

        _homeUiState.update {
            it.copy(
                keyword = placeName,
                isCurrentLocation = false
            )
        }
    }

    fun setIsCurrentLocation() {
        _homeUiState.update {
            it.copy(isCurrentLocation = !it.isCurrentLocation)
        }
    }

    fun showSortModal() {
        _homeModalState.update {
            HomeModalState.ShowSortSheet(homeUiState.value.sortFilters)
        }
    }

    fun showSearchModal() {
        _homeModalState.update {
            HomeModalState.ShowSearchDialog
        }
    }

    fun dismissModal() {
        _homeModalState.update { HomeModalState.Dismiss }
    }

    companion object {
        private val SEOUL_LAT_LNG = LatLng(37.5665, 126.9780)
    }
}

