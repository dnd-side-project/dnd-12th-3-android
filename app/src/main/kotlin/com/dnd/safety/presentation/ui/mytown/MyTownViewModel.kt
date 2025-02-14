package com.dnd.safety.presentation.ui.mytown

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dnd.safety.domain.model.MyTown
import com.dnd.safety.domain.model.SearchResult
import com.dnd.safety.location.LocationService
import com.dnd.safety.presentation.ui.mytown.effect.MyTownModalState
import com.dnd.safety.presentation.ui.mytown.state.MyTownUiState
import com.dnd.safety.utils.Const.SEOUL_LAT_LNG
import com.dnd.safety.utils.Logger
import com.dnd.safety.utils.trigger.TriggerStateFlow
import com.dnd.safety.utils.trigger.triggerStateIn
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyTownViewModel @Inject constructor(
    private val locationService: LocationService
) : ViewModel() {

    val myLocation = locationService
        .requestLocationUpdates()
        .filterNotNull()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = SEOUL_LAT_LNG
        )

    private val _myTownUiState = MutableStateFlow<MyTownUiState>(MyTownUiState.Loading)
    val myTownUiState: TriggerStateFlow<MyTownUiState> = _myTownUiState.onStart {
        fetchMyTowns()
    }.triggerStateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(3000L),
        initialValue = MyTownUiState.Loading
    )

    private val _myTownModalState = MutableStateFlow<MyTownModalState>(MyTownModalState.Dismiss)
    val myTownModalState: StateFlow<MyTownModalState> get() = _myTownModalState

    private fun fetchMyTowns() {
        viewModelScope.launch {
            _myTownUiState.update {
                MyTownUiState.Success(
                    myTowns = listOf(
                        MyTown(
                            id = 1,
                            title = "My Town",
                            latLng = LatLng(37.5665, 126.9780),
                            selected = true
                        )
                    )
                )
            }
        }
    }

    fun addressSelected(searchResult: SearchResult) {
        Logger.d("addressSelected: $searchResult")
    }

    fun addMyTown(myTown: MyTown) {
        // add my town
    }

    fun deleteMyTown(myTownId: Long) {
        // delete my town
    }

    fun selectTown(myTown: MyTown) {
        val uiState = myTownUiState.value as? MyTownUiState.Success ?: return

        viewModelScope.launch {
            _myTownUiState.update {
                MyTownUiState.Success(
                    myTowns = uiState.myTowns.map {
                        it.copy(selected = it.id == myTown.id)
                    }
                )
            }
        }
    }

    fun showTownSearch() {
        _myTownModalState.update {
            MyTownModalState.ShowSearchDialog
        }
    }

    fun showDeleteCheck(myTown: MyTown) {
        _myTownModalState.update {
            MyTownModalState.ShowDeleteCheckDialog(myTown)
        }
    }

    fun dismissModal() {
        _myTownModalState.update {
            MyTownModalState.Dismiss
        }
    }

}

