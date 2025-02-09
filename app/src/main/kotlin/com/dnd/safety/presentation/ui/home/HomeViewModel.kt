package com.dnd.safety.presentation.ui.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dnd.safety.domain.model.BoundingBox
import com.dnd.safety.domain.model.IncidentTypeFilter
import com.dnd.safety.domain.model.Incidents
import com.dnd.safety.domain.model.Point
import com.dnd.safety.domain.model.SortFilter
import com.dnd.safety.domain.repository.IncidentsRepository
import com.dnd.safety.location.LocationService
import com.dnd.safety.presentation.ui.home.component.LocationSource
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

    /**
     * 1. 리스트를 불러오기 위한 화면의 상태
     * 2. 위치 값
     * 3. 지도의 경계에 따른 값.
     *
     * locationState에 따라서 boundingBoxState는 자동적으로 값이 변경된다.
     *
     * 화면의 상태와 boundingBoxState를 합쳐서 어떤 값을 보여줄지 결정한다.
     *
     * 위치 정보는 현재 위치를 계속해서 탐색하는 버튼이 눌러져 있다면, 계속해서 위치를 탐색한다.
     * 만약 버튼이 눌러저 있지 않다면 화면의 이동에 따라 값을 계속해서 변경시킨다.
     *
     * 위치 변경 조건
     * 1. 현재 위치
     * 2. 검색을 통한 위치 조정 <- 이 부분은
     * 3. 지도의 이동은 고려할 필요가 없음 알아서 boudingBoxState가 변경됨
     *
     * locationState는 side effect로 위치가 변경될 경우에 카메라의 위치를 이동시키는데 사용됨.
     *
     * */

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
        updateLocationState(LocationSource.CurrentLocation)
    }

    private var contentJob: Job? = null

    fun updateLocationState(locationSource: LocationSource) {
        contentJob?.cancel()

        when (locationSource) {
            LocationSource.CurrentLocation -> updateLocationByCurrent()
            is LocationSource.Search -> updateLocationBySearch(locationSource)
        }
    }

    private fun updateLocationByCurrent() {
        contentJob = viewModelScope.launch {
            locationService.requestLocationUpdates().collectLatest { location ->
                _locationState.update { location ?: SEOUL_LAT_LNG }
            }
        }
    }

    private fun updateLocationBySearch(locationSource: LocationSource.Search) {
        contentJob = viewModelScope.launch {
            _locationState.update { locationSource.location }
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

    fun setSearchPlace(latLng: LatLng, placeName: String) {
        updateLocationBySearch(LocationSource.Search(latLng))
        _homeUiState.update {
            it.copy(keyword = placeName)
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

