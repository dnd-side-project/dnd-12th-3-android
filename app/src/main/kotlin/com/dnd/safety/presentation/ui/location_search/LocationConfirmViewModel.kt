package com.dnd.safety.presentation.ui.location_search

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.dnd.safety.data.model.Location
import com.dnd.safety.domain.model.MyTown
import com.dnd.safety.domain.model.Point
import com.dnd.safety.domain.repository.MyTownRepository
import com.dnd.safety.domain.repository.SettingRepository
import com.dnd.safety.presentation.navigation.Route
import com.dnd.safety.presentation.ui.location_search.effect.LocationConfirmEffect
import com.skydoves.sandwich.onFailure
import com.skydoves.sandwich.onSuccess
import com.skydoves.sandwich.suspendOnFailure
import com.skydoves.sandwich.suspendOnSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationConfirmViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val myTownRepository: MyTownRepository,
    private val settingRepository: SettingRepository
) : ViewModel() {

    val location = savedStateHandle.toRoute<Route.LocationConfirm>()

    private val _effect = MutableSharedFlow<LocationConfirmEffect>()
    val effect = _effect.asSharedFlow()

    fun complete(
        pushCheck: Boolean
    ) {
        addMyTown()
        updateNotificationSetting(pushCheck)
    }

    private fun addMyTown() {
        viewModelScope.launch {
            myTownRepository.addMyTown(
                title = location.title,
                address = location.address,
                point = Point(
                    x = location.pointX.toDouble(),
                    y = location.pointY.toDouble()
                )
            ).suspendOnSuccess {
                _effect.emit(LocationConfirmEffect.NavigateToMainScreen)
            }.suspendOnFailure {
                _effect.emit(LocationConfirmEffect.ShowSnackBar("오류가 발생했습니다"))
            }
        }
    }

    private fun updateNotificationSetting(isNotificationEnabled: Boolean) {
        viewModelScope.launch {
            settingRepository.updateNotificationSetting(isNotificationEnabled)
        }
    }
}