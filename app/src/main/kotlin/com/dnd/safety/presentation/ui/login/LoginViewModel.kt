package com.dnd.safety.presentation.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dnd.safety.domain.repository.AuthRepository
import com.skydoves.sandwich.suspendOnFailure
import com.skydoves.sandwich.suspendOnSuccess
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
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state = _state.asStateFlow()

    private val _effect = Channel<LoginEffect>()
    val effect = _effect.receiveAsFlow()

    fun loginWithKakao() = viewModelScope.launch {
        _state.update { it.copy(isLoading = true) }

        authRepository.loginWithKakao()
            .suspendOnSuccess {
                _state.update { it.copy(isLoading = false, token = data) }
                _effect.send(LoginEffect.NavigateToNickName)
            }.suspendOnFailure {
                _state.update { it.copy(isLoading = false) }
                _effect.send(LoginEffect.ShowSnackBar("로그인 실패"))
            }
    }

    fun onLoginEvent(event: LoginEvent) = viewModelScope.launch {
        when (event) {
            is LoginEvent.GoogleSignInSuccess -> loginWithGoogle(event.idToken)
            is LoginEvent.GoogleSignInError -> handleError(event.error)
        }
    }

    private suspend fun loginWithGoogle(idToken: String) {
        _state.update { it.copy(isLoading = true) }

        authRepository.signInWithGoogle(idToken)
            .suspendOnSuccess {
                _state.update { it.copy(isLoading = false, token = data) }
                _effect.send(LoginEffect.NavigateToNickName)
            }.suspendOnFailure {
                _state.update { it.copy(isLoading = false) }
                _effect.send(LoginEffect.ShowSnackBar("로그인 실패"))
            }
    }

    private suspend fun handleError(throwable: Throwable) {
        _state.update { it.copy(isLoading = false) }
        _effect.send(LoginEffect.ShowSnackBar(throwable.message ?: "로그인 실패"))
    }
}

sealed class LoginEvent {
    data class GoogleSignInSuccess(val idToken: String) : LoginEvent()
    data class GoogleSignInError(val error: Throwable) : LoginEvent()
}