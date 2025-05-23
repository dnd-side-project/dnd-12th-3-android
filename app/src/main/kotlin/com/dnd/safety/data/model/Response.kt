package com.dnd.safety.data.model

import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.StateFlow

typealias OneTapSignInResponse = Response<BeginSignInResult>
typealias FirebaseSignInResponse = Response<AuthResult>
typealias SignOutResponse = Response<Boolean>
typealias DeleteAccountResponse = Response<Boolean>
typealias AuthStateResponse = StateFlow<FirebaseUser?>

sealed class Response<out T> {
    data object Loading: Response<Nothing>()
    data class Success<out T>(val data: T?): Response<T>()
    data class Failure(val e: Exception): Response<Nothing>()
}