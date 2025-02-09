package com.dnd.safety.presentation.ui.nicknameform

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NicknameFormViewModel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow(NicknameFormState())
    val state = _state.asStateFlow()

    private val _effect = Channel<NicknameFormEffect>()
    val effect = _effect.receiveAsFlow()

    fun updateNickName(newText: String) {
        if (newText.length <= 10) {
            _state.update { it.copy(text = newText) }
        }
    }

    fun navigateToNext() = viewModelScope.launch {
        if (state.value.isButtonEnabled) {
            _state.update { it.copy(isLoading = true) }
            _effect.send(NicknameFormEffect.NavigationToMain)
        }
    }
}