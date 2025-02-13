package com.dnd.safety.presentation.ui.search_address_dialog

import android.content.Context
import android.location.Geocoder
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dnd.safety.domain.model.LawDistrict
import com.dnd.safety.domain.model.SearchResult
import com.dnd.safety.domain.repository.LawDistrictRepository
import com.dnd.safety.utils.Logger
import com.google.android.gms.maps.model.LatLng
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.message
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class SearchAddressDialogViewModel @Inject constructor(
    lawDistrictRepository: LawDistrictRepository,
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

    private val _searchAddressCompleteEffect = MutableSharedFlow<SearchResult>()
    val searchAddressCompleteEffect: SharedFlow<SearchResult> get() = _searchAddressCompleteEffect.asSharedFlow()

    fun textChanged(text: String) {
        searchText.update { text }
    }

    fun getLatLngFromAddress(context: Context, address: String) {
        try {
            val geocoder = Geocoder(context, Locale.getDefault())
            val result = geocoder.getFromLocationName(address, 1)
            result?.firstOrNull()?.let {
                searchAddressComplete(LatLng(it.latitude, it.longitude), address)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            searchError()
        }
    }

    private fun searchAddressComplete(latLng: LatLng, address: String) {
        _searchAddressCompleteEffect.tryEmit(SearchResult(address, latLng))
    }

    private fun searchError() {
        textChanged(searchText.value)
    }
}

