package com.dnd.safety.domain.repository

import com.dnd.safety.data.model.AuthStateResponse
import com.dnd.safety.data.model.DeleteAccountResponse
import com.dnd.safety.data.model.FirebaseSignInResponse
import com.dnd.safety.data.model.OneTapSignInResponse
import com.dnd.safety.data.model.SignOutResponse
import com.google.android.gms.auth.api.identity.SignInCredential
import com.skydoves.sandwich.ApiResponse
import kotlinx.coroutines.CoroutineScope

interface AuthRepository {
    suspend fun loginWithKakao(): ApiResponse<String>
    suspend fun signInWithGoogle(idToken: String): ApiResponse<String>

    fun getAuthState(viewModelScope: CoroutineScope): AuthStateResponse

    suspend fun verifyGoogleSignIn(): Boolean

    suspend fun signInAnonymously(): FirebaseSignInResponse

    suspend fun onTapSignIn(): OneTapSignInResponse

    suspend fun signInWithGoogle(credential: SignInCredential): FirebaseSignInResponse

    suspend fun signOut(): SignOutResponse

    suspend fun authorizeGoogleSignIn(): String?

    suspend fun deleteUserAccount(googleIdToken: String?): DeleteAccountResponse

    fun checkNeedsReAuth(): Boolean
}