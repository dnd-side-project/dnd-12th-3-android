package com.dnd.safety.presentation.ui.location_search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dnd.safety.domain.model.LawDistrict
import com.dnd.safety.domain.model.MyTown
import com.dnd.safety.domain.model.Point
import com.dnd.safety.domain.repository.LawDistrictRepository
import com.dnd.safety.domain.usecase.FetchLocationUseCase
import com.dnd.safety.presentation.ui.location_search.effect.LocationSearchEffect
import com.dnd.safety.utils.Logger
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.message
import com.skydoves.sandwich.onFailure
import com.skydoves.sandwich.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationSearchViewModel @Inject constructor(
    private val lawDistrictRepository: LawDistrictRepository,
    private val fetchLocationUseCase: FetchLocationUseCase,
) : ViewModel() {

    var searchText = MutableStateFlow("")
        private set

    @OptIn(FlowPreview::class)
    val lawDistricts: StateFlow<List<LawDistrict>> = searchText
        .debounce(300)
        .filter { it.length >= 2 }
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


    private val _effect = Channel<LocationSearchEffect>()
    val effect = _effect.receiveAsFlow()

    fun textChanged(text: String) {
        searchText.update { text }
    }

    fun getPoint(lawDistrict: LawDistrict) {
        viewModelScope.launch {
            lawDistrictRepository.getPoint(
                lawDistrict.pointDto
            ).onSuccess {
                searchAddressComplete(data, lawDistrict.name, lawDistrict.address)
            }.onFailure {
                searchError()
            }
        }
    }

    private fun searchAddressComplete(point: Point, name: String, address: String) {
        viewModelScope.launch {
            _effect.send(
                LocationSearchEffect.NavigateToLocationConfirm(
                    MyTown(
                        id = 0,
                        title = name,
                        address = address,
                        point = point,
                        selected = false
                    )
                )
            )
        }
    }

    private fun searchError() {
        textChanged(searchText.value)
    }
}