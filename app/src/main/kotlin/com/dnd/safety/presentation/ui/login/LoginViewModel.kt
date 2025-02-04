package com.dnd.safety.presentation.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dnd.safety.domain.usecase.GoogleSignInUseCase
import com.dnd.safety.domain.usecase.KakaoLoginUseCase
import com.dnd.safety.presentation.navigation.utils.GoogleSignInHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val kakaoLoginUseCase: KakaoLoginUseCase,
    private val googleSignInUseCase: GoogleSignInUseCase,
    private val googleSignInHelper: GoogleSignInHelper,
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
    fun onLoginEvent(event: LoginEvent) = viewModelScope.launch {
        when (event) {
            is LoginEvent.GoogleSignInSuccess -> loginWithGoogle(event.idToken)
            is LoginEvent.GoogleSignInError -> handleError(event.error)
        }
    }

    private suspend fun loginWithGoogle(idToken: String) {
        _state.update { it.copy(isLoading = true) }

        googleSignInUseCase(idToken).fold(
            onSuccess = { token ->
                _state.update { it.copy(isLoading = false, token = token) }
                _effect.send(LoginEffect.NavigateToNickName)
            },
            onFailure = { throwable ->
                _state.update { it.copy(isLoading = false) }
                when(throwable) {
                    is SocketTimeoutException -> {
                        _effect.send(LoginEffect.ShowToast("네트워크 연결이 불안정합니다"))
                    }
                    else -> {
                        _effect.send(LoginEffect.ShowToast("일시적인 오류가 발생했습니다"))
                    }
                }
            }
        )
    }

    private suspend fun handleError(throwable: Throwable) {
        _state.update { it.copy(isLoading = false) }
        _effect.send(LoginEffect.ShowToast(throwable.message ?: "로그인 실패"))
    }

}

sealed class LoginEvent {
    data class GoogleSignInSuccess(val idToken: String) : LoginEvent()
    data class GoogleSignInError(val error: Throwable) : LoginEvent()
}