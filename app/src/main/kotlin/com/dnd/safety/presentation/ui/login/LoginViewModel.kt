package com.dnd.safety.presentation.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dnd.safety.domain.usecase.KakaoLoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val kakaoLoginUseCase: KakaoLoginUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(LoginState())
    val state = _state.asStateFlow()

    private val _effect = Channel<LoginEffect>()
    val effect = _effect.receiveAsFlow()

    fun loginWithKakao() = viewModelScope.launch {
        _state.update { it.copy(isLoading = true) }

        kakaoLoginUseCase().fold(
            onSuccess = {
                _state.update { it.copy(isLoading = false) }
                _effect.send(LoginEffect.NavigateToNickName)
            },
            onFailure = { throwable ->
                _state.update { it.copy(isLoading = false) }
                _effect.send(LoginEffect.ShowToast(throwable.message ?: "로그인 실패"))
            }
        )
    }

}