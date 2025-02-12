package com.dnd.safety.presentation.ui.mytown

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dnd.safety.domain.model.MyTown
import com.dnd.safety.location.LocationService
import com.dnd.safety.presentation.ui.home.state.HomeModalState
import com.dnd.safety.presentation.ui.mytown.effect.MyTownModalState
import com.dnd.safety.presentation.ui.mytown.state.MyTownUiState
import com.dnd.safety.utils.Const.SEOUL_LAT_LNG
import com.dnd.safety.utils.trigger.TriggerStateFlow
import com.dnd.safety.utils.trigger.triggerStateIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class MyTownViewModel @Inject constructor(
    locationService: LocationService
) : ViewModel() {

    val myLocation = locationService
        .requestLocationUpdates()
        .filterNotNull()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = SEOUL_LAT_LNG
        )

    val myTownUiState: TriggerStateFlow<MyTownUiState> = flowOf(
        MyTownUiState.Success(
            listOf(
                MyTown(
                    id = 1,
                    title = "My Town",
                    selected = true
                )
            )
        )
    ).triggerStateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(3000L),
        initialValue = MyTownUiState.Loading
    )

    private val _myTownModalState = MutableStateFlow<MyTownModalState>(MyTownModalState.Dismiss)
    val myTownModalState: StateFlow<MyTownModalState> get() = _myTownModalState


    fun showTownSearch() {
        _myTownModalState.update {
            MyTownModalState.ShowSearchDialog
        }
    }

    fun deleteMyTown(myTown: MyTown) {
        // delete my town
    }

    fun selectMyTown(myTown: MyTown) {
        // select my town
    }

    fun dismissModal() {
        _myTownModalState.update {
            MyTownModalState.Dismiss
        }
    }

}

