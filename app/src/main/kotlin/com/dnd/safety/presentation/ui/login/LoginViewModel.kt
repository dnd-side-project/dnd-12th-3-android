package com.dnd.safety.presentation.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dnd.safety.data.model.DataProvider
import com.dnd.safety.data.model.Response
import com.dnd.safety.domain.repository.AuthRepository
import com.dnd.safety.utils.Logger
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.identity.SignInCredential
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
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    val oneTapClient: SignInClient
) : ViewModel() {

    val currentUser = getAuthState()

    var isLoading = MutableStateFlow(false)
        private set

    private val _state: MutableStateFlow<LoginUiState> = MutableStateFlow(LoginUiState.Initializing)
    val state: StateFlow<LoginUiState> get() = _state

    private val _effect = Channel<LoginEffect>()
    val effect = _effect.receiveAsFlow()

    init {
        viewModelScope.launch {
            if (authRepository.checkKakaoLogin()) {
                checkIsNeedToEnterLocation()
            } else {
                loginRequired()
            }
        }
    }

    fun changeLoadingState(isLoading: Boolean) {
        this.isLoading.value = isLoading
    }

    fun loginRequired() {
        _state.update { LoginUiState.NeedLogin }
    }

    fun checkIsNeedToEnterLocation() {
        viewModelScope.launch {
            if (true) {
                _effect.send(LoginEffect.NavigateToHome)
            } else {
                _effect.send(LoginEffect.NavigateToLocation)
            }
        }
    }

    private fun getAuthState() = authRepository.getAuthState(viewModelScope)

    fun signInAnonymously() = CoroutineScope(Dispatchers.IO).launch {
        DataProvider.anonymousSignInResponse = Response.Loading
        DataProvider.anonymousSignInResponse = authRepository.signInAnonymously()
    }

    fun oneTapSignIn() = CoroutineScope(Dispatchers.IO).launch {
        DataProvider.oneTapSignInResponse = Response.Loading
        DataProvider.oneTapSignInResponse = authRepository.onTapSignIn()
    }

    fun signInWithGoogle(credentials: SignInCredential) = CoroutineScope(Dispatchers.IO).launch {
        DataProvider.googleSignInResponse = Response.Loading
        DataProvider.googleSignInResponse = authRepository.signInWithGoogle(credentials)
    }

    fun signOut() = CoroutineScope(Dispatchers.IO).launch {
        DataProvider.signOutResponse = Response.Loading
        DataProvider.signOutResponse = authRepository.signOut()
    }

    fun checkNeedsReAuth() = CoroutineScope(Dispatchers.IO).launch {
        if (authRepository.checkNeedsReAuth()) {
            // Authorize google sign in
            val idToken = authRepository.authorizeGoogleSignIn()
            if (idToken != null) {
                deleteAccount(idToken)
            }
            else {
                // If failed initiate oneTap sign in flow
                // deleteAccount(googleIdToken:) will be called from oneTap result callback
                oneTapSignIn()
                Logger.i("OneTapSignIn")
            }
        } else {
            deleteAccount(null)
        }
    }

    fun deleteAccount(googleIdToken: String?) = CoroutineScope(Dispatchers.IO).launch {
        Logger.i("Deleting Account...")
        DataProvider.deleteAccountResponse = Response.Loading
        DataProvider.deleteAccountResponse = authRepository.deleteUserAccount(googleIdToken)
    }

    fun loginWithKakao() {
        changeLoadingState(true)
        viewModelScope.launch {
            authRepository.loginWithKakao()
                .onSuccess {
                    checkIsNeedToEnterLocation()
                    changeLoadingState(false)
                }.suspendOnFailure {
                    _effect.send(LoginEffect.ShowSnackBar("카카오 로그인에 실패했습니다"))
                    changeLoadingState(false)
                }
        }
    }
}