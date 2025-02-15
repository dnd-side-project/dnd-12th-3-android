package com.dnd.safety.presentation.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dnd.safety.data.model.DataProvider
import com.dnd.safety.data.model.Response
import com.dnd.safety.domain.repository.AuthRepository
import com.dnd.safety.utils.Logger
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.identity.SignInCredential
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    val oneTapClient: SignInClient
) : ViewModel() {

    val currentUser = getAuthState()

    private val _state = MutableStateFlow(LoginState())
    val state = _state.asStateFlow()

    private val _effect = Channel<LoginEffect>()
    val effect = _effect.receiveAsFlow()

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
}

sealed class LoginEvent {
    data class GoogleSignInSuccess(val idToken: String) : LoginEvent()
    data class GoogleSignInError(val error: Throwable) : LoginEvent()
}