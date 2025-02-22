package com.dnd.safety.presentation.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dnd.safety.domain.model.Setting
import com.dnd.safety.domain.repository.SettingRepository
import com.dnd.safety.utils.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
) : ViewModel() {

    val visiblePermissionDialogQueue = MutableStateFlow<List<String>>(emptyList())

    fun dismissDialog() {
        visiblePermissionDialogQueue.update { emptyList() }
    }

    fun onPermissionResult(
        permissions: Map<String, Boolean>
    ) {
        viewModelScope.launch {
            permissions.forEach { (permission, isGranted) ->
                if(!isGranted && !visiblePermissionDialogQueue.value.contains(permission)) {
                    visiblePermissionDialogQueue.update {
                        it + permission
                    }
                }
            }
        }
    }
}