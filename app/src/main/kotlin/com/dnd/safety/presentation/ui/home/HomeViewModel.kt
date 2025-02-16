package com.dnd.safety.presentation.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dnd.safety.domain.model.BoundingBox
import com.dnd.safety.domain.model.IncidentTypeFilter
import com.dnd.safety.domain.model.Point
import com.dnd.safety.domain.repository.IncidentsRepository
import com.dnd.safety.location.LocationService
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
    locationService: LocationService,
    incidentsRepository: IncidentsRepository,
) : ViewModel() {

    val myLocation = locationService
        .requestLocationUpdates()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = null
        )

    var keyword = MutableStateFlow("")
        private set

    private val _cameraLocationState = MutableStateFlow<LatLng?>(null)
    val cameraLocationState: StateFlow<LatLng?> get() = _cameraLocationState

    private val boundingBoxState = MutableStateFlow<BoundingBoxState>(BoundingBoxState.NotInitialized)

    private val _homeUiState = MutableStateFlow(HomeUiState())
    val homeUiState: StateFlow<HomeUiState> get() = _homeUiState

    val incidentsState: StateFlow<IncidentsState> = cameraLocationState.map { location ->
        if (location != null) {
                when (val incidents = incidentsRepository.getIncidents(location)) {
                    is ApiResponse.Success -> {
                        IncidentsState.Success(incidents.data)
                    }
                    is ApiResponse.Failure.Error -> {
                        Logger.e(incidents.message())
                        IncidentsState.Loading
                    }
                    is ApiResponse.Failure.Exception -> {
                        Logger.e("${incidents.message}")
                        IncidentsState.Loading
                    }
                }
        } else {
            IncidentsState.Loading
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = IncidentsState.Loading
    )

    private val _homeModalState = MutableStateFlow<HomeModalState>(HomeModalState.Dismiss)
    val homeModalState: StateFlow<HomeModalState> get() = _homeModalState

    init {
        initLocation()
    }

    private fun initLocation() {
        viewModelScope.launch {
            myLocation.collectLatest { location ->
                if (location != null) {
                    _cameraLocationState.update { location }
                    cancel()
                }
            }
        }
    }

    private fun updateLocationBySearch(location: LatLng) {
        viewModelScope.launch {
            _cameraLocationState.update { location }
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

    fun setSearchPlace(latLng: LatLng, placeName: String = "") {
        updateLocationBySearch(latLng)

        keyword.update { placeName }
    }

    fun setLocationCurrent() {
        _cameraLocationState.update { myLocation.value ?: SEOUL_LAT_LNG }
    }

    fun showSearchModal() {
        _homeModalState.update {
            HomeModalState.ShowSearchDialog
        }
    }

    fun dismissModal() {
        _homeModalState.update { HomeModalState.Dismiss }
    }

}

