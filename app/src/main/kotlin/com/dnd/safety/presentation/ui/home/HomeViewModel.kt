package com.dnd.safety.presentation.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dnd.safety.data.location.LocationService
import com.dnd.safety.domain.model.BoundingBox
import com.dnd.safety.domain.model.Incident
import com.dnd.safety.domain.model.IncidentTypeFilter
import com.dnd.safety.domain.model.Point
import com.dnd.safety.domain.repository.IncidentListRepository
import com.dnd.safety.presentation.ui.home.effect.HomeUiEffect
import com.dnd.safety.presentation.ui.home.state.BoundingBoxState
import com.dnd.safety.presentation.ui.home.state.HomeModalState
import com.dnd.safety.presentation.ui.home.state.HomeUiState
import com.dnd.safety.presentation.ui.home.state.IncidentsState
import com.dnd.safety.utils.Const.SEOUL_LAT_LNG
import com.dnd.safety.utils.Logger
import com.google.android.gms.maps.model.LatLng
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.message
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    locationService: LocationService,
    incidentListRepository: IncidentListRepository,
) : ViewModel() {

    val myLocation = locationService.requestLocationUpdates()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = null
        )

    var keyword = MutableStateFlow("")
        private set

    private val boundingBoxState = MutableStateFlow<BoundingBoxState>(BoundingBoxState.NotInitialized)

    private val _homeUiState = MutableStateFlow(HomeUiState())
    val homeUiState: StateFlow<HomeUiState> get() = _homeUiState

    val incidentsState: StateFlow<IncidentsState> = combine(
        boundingBoxState,
        homeUiState
    ) { boundingBoxState, homeUiState ->
        when (boundingBoxState) {
            BoundingBoxState.NotInitialized -> IncidentsState.Loading
            is BoundingBoxState.Success -> {
                val incidents = incidentListRepository.getIncidents(boundingBoxState.boundingBox, myLocation.value ?: SEOUL_LAT_LNG)
                when (incidents) {
                    is ApiResponse.Success -> IncidentsState.Success(incidents.data)
                    is ApiResponse.Failure -> {

                        IncidentsState.Loading
                    }
                }
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = IncidentsState.Loading
    )

    private val _homeModalState = MutableStateFlow<HomeModalState>(HomeModalState.Dismiss)
    val homeModalState: StateFlow<HomeModalState> get() = _homeModalState

    private val _homeUiEffect = MutableSharedFlow<HomeUiEffect>()
    val homeUiEffect: SharedFlow<HomeUiEffect> get() = _homeUiEffect

    init {
        initLocation()
    }

    private fun initLocation() {
        viewModelScope.launch {
            myLocation.collectLatest { location ->
                location?.let {
                    moveCameraToLocation(it)
                    cancel()
                }
            }
        }
    }

    private fun updateLocationBySearch(location: LatLng) {
        viewModelScope.launch {
            moveCameraToLocation(location)
        }
    }

    fun updateBoundingBoxState(
        topLeft: LatLng, bottomRight: LatLng
    ) {
        boundingBoxState.update {
            BoundingBoxState.Success(
                BoundingBox(
                    topRight = Point(topLeft), bottomleft = Point(bottomRight)
                )
            )
        }
    }

    fun setIncidentTypeFilter(type: IncidentTypeFilter) {
        _homeUiState.update {
            it.copy(typeFilters = it.typeFilters.map { filter ->
                filter.copy(isSelected = filter == type)
            })
        }
    }

    fun setSearchPlace(latLng: LatLng, placeName: String = "") {
        updateLocationBySearch(latLng)

        keyword.update { placeName }
    }

    fun setLocationCurrent() {
        myLocation.value?.let {
            moveCameraToLocation(it)
        }
    }

    fun likeIncident(incident: Incident) {
        viewModelScope.launch {}
    }

    fun moveCameraToLocation(latLng: LatLng) {
        viewModelScope.launch {
            _homeUiEffect.emit(HomeUiEffect.MoveCameraToLocation(latLng))
        }
    }

    fun showSearchModal() {
        _homeModalState.update {
            HomeModalState.ShowSearchDialog
        }
    }

    fun showIncidentDetail(incident: Incident) {
        viewModelScope.launch {
            _homeUiEffect.emit(HomeUiEffect.ShowIncidentDetail(incident))
        }
    }

    fun dismissModal() {
        _homeModalState.update { HomeModalState.Dismiss }
    }
}

