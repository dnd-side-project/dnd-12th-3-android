package com.dnd.safety.presentation.ui.search_address_dialog

import android.content.Context
import android.location.Address
import android.location.Geocoder
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dnd.safety.data.location.LocationService
import com.dnd.safety.domain.model.LawDistrict
import com.dnd.safety.domain.model.Point
import com.dnd.safety.domain.model.SearchResult
import com.dnd.safety.domain.repository.LawDistrictRepository
import com.dnd.safety.utils.Logger
import com.google.android.gms.maps.model.LatLng
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.message
import com.skydoves.sandwich.onFailure
import com.skydoves.sandwich.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class SearchAddressDialogViewModel @Inject constructor(
    private val lawDistrictRepository: LawDistrictRepository,
    private val locationService: LocationService
) : ViewModel() {


    var searchText = MutableStateFlow("")
        private set

    @OptIn(FlowPreview::class)
    val lawDistricts: StateFlow<List<LawDistrict>> = searchText
        .debounce(300)
        .filterNot(String::isEmpty)
        .map {
            when (val data = lawDistrictRepository.getLawDistricts(it)) {
                is ApiResponse.Success -> data.data
                is ApiResponse.Failure.Error -> {
                    Logger.e(data.message())
                    emptyList()
                }

                is ApiResponse.Failure.Exception -> {
                    Logger.e(data.message())
                    emptyList()
                }
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = emptyList()
        )

    private val _searchAddressCompleteEffect = MutableSharedFlow<SearchAddressEffect>()
    val searchAddressCompleteEffect: SharedFlow<SearchAddressEffect> get() = _searchAddressCompleteEffect

    fun textChanged(text: String) {
        searchText.update { text }
    }

    fun getPoint(lawDistrict: LawDistrict) {
        viewModelScope.launch {
            lawDistrictRepository.getPoint(
                lawDistrict.pointDto
            ).onSuccess {
                searchAddressComplete(data, lawDistrict)
            }.onFailure {
                searchError()
            }
        }
    }

    fun searchToCurrentLocation(context: Context) {
        viewModelScope.launch {
            val location = locationService.getCurrentLocation()

            val address = getLastAddress(location, context)

            if (address.isEmpty()) {
                searchError()
                showSnackBar("현재 위치에 대한 주소를 가져올 수 없습니다")
            } else {
                searchText.value = address
            }
        }
    }

    private fun searchAddressComplete(point: Point, lawDistrict: LawDistrict) {
        viewModelScope.launch {
            _searchAddressCompleteEffect.emit(
                SearchAddressEffect.SearchAddressComplete(
                    SearchResult(
                        name = lawDistrict.name.ifBlank { lawDistrict.address },
                        roadAddress = lawDistrict.roadAddress,
                        lotAddress = lawDistrict.lotAddress,
                        point = point
                    )
                )
            )
        }
    }

    private fun showSnackBar(message: String) {
        viewModelScope.launch {
            _searchAddressCompleteEffect.emit(
                SearchAddressEffect.ShowSnackBar(message)
            )
        }
    }

    private fun searchError() {
        textChanged(searchText.value)
    }

    private fun getLastAddress(location: LatLng?, context: Context): String {
        if (location == null) {
            return ""
        }

        val geocoder = Geocoder(context, Locale.getDefault())
        var address = ""

        try {
            val geoAddress: Address? = geocoder.getFromLocation(
                location.latitude,
                location.longitude,
                1
            )?.get(0)

            geoAddress?.let {
                address = changeAddrtoString(geoAddress)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Logger.e("현재 주소를 가져올 수 없음: ${e.message}")
        }

        return address
    }

    private fun changeAddrtoString(addr: Address): String {
        var address = ""

        try {
            if (addr.getAddressLine(0).isNullOrBlank()) {
                address = if (!addr.adminArea.isNullOrBlank()) {
                    addr.adminArea
                } else {
                    ""
                } + if (!addr.locality.isNullOrBlank() && addr.adminArea != addr.locality) {
                    " " + addr.locality
                } else {
                    ""
                } + if (!addr.thoroughfare.isNullOrBlank()) {
                    " " + addr.thoroughfare
                } else {
                    ""
                } + if (!addr.featureName.isNullOrBlank()) {
                    " " + addr.featureName
                } else {
                    ""
                }
            } else {
                address = addr.getAddressLine(0).replace(addr.countryName, "").trim()
            }
        } catch (e: Exception) {
            Logger.e("주소 변환 실패: ${e.message}")
        }

        return address
    }
}

@Stable
sealed interface SearchAddressEffect {

    @Immutable
    data class SearchAddressComplete(val searchResult: SearchResult) : SearchAddressEffect

    @Immutable
    data class ShowSnackBar(val message: String) : SearchAddressEffect
}

