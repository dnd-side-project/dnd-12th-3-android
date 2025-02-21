package com.dnd.safety.presentation.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dnd.safety.data.model.DataProvider
import com.dnd.safety.data.model.Response
import com.dnd.safety.domain.repository.AuthRepository
import com.dnd.safety.domain.repository.LoginRepository
import com.dnd.safety.domain.usecase.CheckLocationNeedUsecase
import com.dnd.safety.domain.usecase.CheckTokenUsecase
import com.dnd.safety.domain.usecase.GoogleLoginUsecase
import com.dnd.safety.domain.usecase.KakaoLoginUsecase
import com.dnd.safety.domain.usecase.SendTokenUsecase
import com.dnd.safety.utils.Logger
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.identity.SignInCredential
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import com.skydoves.sandwich.onSuccess
import com.skydoves.sandwich.suspendOnFailure
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val checkTokenUsecase: CheckTokenUsecase,
    private val checkLocationNeedUsecase: CheckLocationNeedUsecase,
    private val googleLoginUsecase: GoogleLoginUsecase,
    private val kakaoLoginUsecase: KakaoLoginUsecase,
    private val sendTokenUsecase: SendTokenUsecase
) : ViewModel() {

    var isLoading = MutableStateFlow(false)
        private set

    var name = MutableStateFlow("")
        private set

    var email = MutableStateFlow("")
        private set

    private val _state: MutableStateFlow<LoginUiState> = MutableStateFlow(LoginUiState.Initializing)
    val state: StateFlow<LoginUiState> get() = _state

    private val _effect = Channel<LoginEffect>()
    val effect = _effect.receiveAsFlow()

    init {
        viewModelScope.launch {
            val isLoginNeed = !checkTokenUsecase()

            if (isLoginNeed) {
                loginRequired()
            } else {
                checkIsNeedToEnterLocation()
            }
        }
    }

    fun changeLoadingState(isLoading: Boolean) {
        this.isLoading.value = isLoading
    }

    fun loginRequired() {
        _state.update { LoginUiState.NeedLogin }
    }

    fun loginByKakao(token: String) {
        viewModelScope.launch {
            kakaoLoginUsecase(
                kakaoToken = token,
                onSuccess = {
                    checkIsNeedToEnterLocation()
                    changeLoadingState(false)
                },
                onError = ::sendFailMessage
            )
        }
    }

    fun loginByGoogle(token: String) {
        viewModelScope.launch {
            googleLoginUsecase(
                googleToken = token,
                onSuccess = {
                    checkIsNeedToEnterLocation()
                    changeLoadingState(false)
                },
                onError = ::sendFailMessage
            )
        }
    }

    private fun sendFailMessage(message: String) {
        viewModelScope.launch {
            _effect.send(LoginEffect.ShowSnackBar(message))
            changeLoadingState(false)
        }
    }

    fun checkIsNeedToEnterLocation() {
        viewModelScope.launch {
            val isNeedToEnterLocation = checkLocationNeedUsecase()

            if (isNeedToEnterLocation) {
                _effect.send(LoginEffect.NavigateToLocation)
            } else {
                _effect.send(LoginEffect.NavigateToHome)
            }
        }
    }

    fun loginWithKakao() {
        changeLoadingState(true)
        viewModelScope.launch {
            authRepository.loginWithKakao()
                .onSuccess {
                    loginByKakao(data)
                }.suspendOnFailure {
                    _effect.send(LoginEffect.ShowSnackBar("카카오 로그인에 실패했습니다"))
                    changeLoadingState(false)
                }
        }
    }

    fun sendToken() {
        viewModelScope.launch {
            if (name.value.isBlank()) {
                _effect.send(LoginEffect.ShowSnackBar("이름을 입력해주세요"))
                return@launch
            }

            if (email.value.isBlank()) {
                _effect.send(LoginEffect.ShowSnackBar("이메일을 입력해주세요"))
                return@launch
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email.value).matches()) {
                _effect.send(LoginEffect.ShowSnackBar("이메일 형식이 올바르지 않습니다"))
                return@launch
            }

            sendTokenUsecase(
                name = name.value,
                email = email.value,
                onSuccess = {
                    checkIsNeedToEnterLocation()
                    changeLoadingState(false)
                },
                onError = ::sendFailMessage
            )
        }
    }

    fun onNameChange(name: String) {
        this.name.value = name
    }

    fun onEmailChange(email: String) {
        this.email.value = email
    }
}