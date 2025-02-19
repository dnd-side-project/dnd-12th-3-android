package com.dnd.safety.presentation.ui.myPage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dnd.safety.domain.model.MyTown
import com.dnd.safety.domain.model.SearchResult
import com.dnd.safety.domain.repository.MyTownRepository
import com.dnd.safety.data.location.LocationService
import com.dnd.safety.presentation.ui.myPage.effect.MyTownModalState
import com.dnd.safety.presentation.ui.myPage.effect.MyTownUiEffect
import com.dnd.safety.presentation.ui.myPage.state.MyTownUiState
import com.dnd.safety.utils.Const.SEOUL_LAT_LNG
import com.dnd.safety.utils.Logger
import com.dnd.safety.utils.trigger.TriggerStateFlow
import com.dnd.safety.utils.trigger.triggerStateIn
import com.skydoves.sandwich.onFailure
import com.skydoves.sandwich.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
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
    private val myTownRepository: MyTownRepository,
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

    private val _myTownUiState = MutableStateFlow<MyTownUiState>(MyTownUiState.Loading)
    val myTownUiState: TriggerStateFlow<MyTownUiState> = _myTownUiState
        .onStart { getMyTowns() }
        .triggerStateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(3000L),
            initialValue = MyTownUiState.Loading
        )

    private val _myTownModalState = MutableStateFlow<MyTownModalState>(MyTownModalState.Dismiss)
    val myTownModalState: StateFlow<MyTownModalState> get() = _myTownModalState

    private val _myTownUiEffect = MutableSharedFlow<MyTownUiEffect>()
    val myTownUiEffect: SharedFlow<MyTownUiEffect> get() = _myTownUiEffect

    private fun getMyTowns() {
        viewModelScope.launch {
            myTownRepository.getMyTownList()
                .onSuccess {
                    _myTownUiState.update {
                        when (it) {
                            MyTownUiState.Loading -> {
                                MyTownUiState.Success(
                                    data.mapIndexed { index, myTown ->
                                        myTown.copy(selected = index == 0)
                                    }
                                )
                            }

                            is MyTownUiState.Success -> MyTownUiState.Success(
                                data.mapIndexed { index, myTown ->
                                    myTown.copy(selected = data.lastIndex == index)
                                }
                            )
                        }
                    }
                }
                .onFailure {
                    showSnackBar("오류가 발생했습니다")
                }
        }
    }

    fun addMyTown(searchResult: SearchResult) {
        viewModelScope.launch {
            myTownRepository.addMyTown(
                title = searchResult.name,
                address = searchResult.lotAddress,
                point = searchResult.point
            ).onSuccess {
                myTownUiState.restart()
                showSnackBar("내 동네가 추가되었습니다")
            }.onFailure {
                showSnackBar("내 동네 추가에 실패했습니다")
            }
        }
    }

    fun deleteMyTown(myTownId: Long) {
        viewModelScope.launch {
            myTownRepository.deleteMyTown(myTownId)
                .onSuccess {
                    myTownUiState.restart()
                    showSnackBar("내 동네가 삭제되었습니다")
                }
                .onFailure {
                    showSnackBar("내 동네 삭제에 실패했습니다")
                }
        }
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

    fun showSnackBar(message: String) {
        viewModelScope.launch {
            _myTownUiEffect.emit(MyTownUiEffect.ShowSnackBar(message))
        }
    }

    fun dismissModal() {
        _myTownModalState.update {
            MyTownModalState.Dismiss
        }
    }

}

