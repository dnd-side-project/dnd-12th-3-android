package com.dnd.safety.presentation.ui.myPage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dnd.safety.data.local.datastore.model.UserPreferences
import com.dnd.safety.domain.model.Setting
import com.dnd.safety.domain.repository.SettingRepository
import com.dnd.safety.domain.repository.UserInfoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val settingRepository: SettingRepository,
    private val userInfoRepository: UserInfoRepository
) : ViewModel() {

    var name: String = ""
        private set

    val setting: StateFlow<Setting> = settingRepository
        .getSettingFlow()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = Setting()
        )

    init {
        viewModelScope.launch {
            name = userInfoRepository.getName()
        }
    }

    fun updateNotificationSetting(value: Boolean) {
        viewModelScope.launch {
            settingRepository.updateNotificationSetting(value)
        }
    }
}