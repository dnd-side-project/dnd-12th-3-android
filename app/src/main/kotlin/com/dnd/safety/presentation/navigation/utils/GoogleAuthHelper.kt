package com.dnd.safety.presentation.navigation.utils

import android.content.Context
import android.content.Intent
import com.dnd.safety.BuildConfig
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.tasks.Task
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GoogleSignInHelper @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private val signInClient = Identity.getSignInClient(context)

    fun getSignInIntent(): Task<BeginSignInResult> {
        return signInClient.beginSignIn(
            BeginSignInRequest.builder()
                .setPasswordRequestOptions(
                    BeginSignInRequest.PasswordRequestOptions.builder()
                        .setSupported(false)
                        .build()
                )
                .setGoogleIdTokenRequestOptions(
                    BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        .setServerClientId(BuildConfig.GOOGLE_CLIENT_ID)
                        .setFilterByAuthorizedAccounts(false)
                        .build()
                )
                .setAutoSelectEnabled(false)
                .build()
        )
    }

    fun getSignInResult(intent: Intent): SignInResult {
        return try {
            val credential = signInClient.getSignInCredentialFromIntent(intent)
            val idToken = credential.googleIdToken
            if (idToken != null) {
                SignInResult(
                    data = GoogleSignInData(idToken = idToken),
                    errorMessage = null
                )
            } else {
                SignInResult(
                    data = null,
                    errorMessage = "Google ID Token이 없습니다."
                )
            }
        } catch (e: Exception) {
            SignInResult(
                data = null,
                errorMessage = e.message
            )
        }
    }
}

data class SignInResult(
    val data: GoogleSignInData?,
    val errorMessage: String?
)

data class GoogleSignInData(
    val idToken: String
)