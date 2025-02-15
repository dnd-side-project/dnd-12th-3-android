package com.dnd.safety.presentation.ui.search_address_dialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dnd.safety.domain.model.LawDistrict
import com.dnd.safety.domain.model.Point
import com.dnd.safety.domain.model.SearchResult
import com.dnd.safety.domain.repository.LawDistrictRepository
import com.dnd.safety.utils.Logger
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.message
import com.skydoves.sandwich.onError
import com.skydoves.sandwich.onFailure
import com.skydoves.sandwich.onSuccess
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
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchAddressDialogViewModel @Inject constructor(
    private val lawDistrictRepository: LawDistrictRepository,
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
    val searchAddressCompleteEffect: SharedFlow<SearchResult> get() = _searchAddressCompleteEffect

    fun textChanged(text: String) {
        searchText.update { text }
    }

    fun getPoint(lawDistrict: LawDistrict) {
        viewModelScope.launch {
            lawDistrictRepository.getPoint(
                lawDistrict.pointDto
            ).onSuccess {
                searchAddressComplete(data, lawDistrict.address, lawDistrict.name)
            }.onFailure {
                searchError()
            }
        }
    }

    private fun searchAddressComplete(point: Point, address: String, name: String) {
        viewModelScope.launch {
            _searchAddressCompleteEffect.emit(SearchResult(address, name, point))
        }
    }

    private fun searchError() {
        textChanged(searchText.value)
    }
}

