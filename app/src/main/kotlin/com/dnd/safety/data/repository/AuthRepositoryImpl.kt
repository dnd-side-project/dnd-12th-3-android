package com.dnd.safety.data.repository

import android.util.Log
import com.dnd.safety.data.model.DataProvider
import com.dnd.safety.data.model.DeleteAccountResponse
import com.dnd.safety.data.model.FirebaseSignInResponse
import com.dnd.safety.data.model.OneTapSignInResponse
import com.dnd.safety.data.model.Response
import com.dnd.safety.data.model.SignOutResponse
import com.dnd.safety.data.model.isWithinPast
import com.dnd.safety.data.model.request.GoogleLogInRequest
import com.dnd.safety.data.remote.api.GoogleAuthService
import com.dnd.safety.di.AuthModule.SIGN_IN_REQUEST
import com.dnd.safety.di.AuthModule.SIGN_UP_REQUEST
import com.dnd.safety.domain.datasource.KakaoLoginDataSource
import com.dnd.safety.domain.repository.AuthRepository
import com.dnd.safety.utils.Logger
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.identity.SignInCredential
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.GoogleAuthProvider
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.mapSuccess
import com.skydoves.sandwich.message
import com.skydoves.sandwich.onFailure
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.tasks.await
import java.util.Date
import javax.inject.Inject
import javax.inject.Named

class AuthRepositoryImpl @Inject constructor(
    private val kakaoLoginDataSource: KakaoLoginDataSource,
    private val googleAuthService: GoogleAuthService,
    private val auth: FirebaseAuth,
    private var oneTapClient: SignInClient,
    private var googleSignInClient: GoogleSignInClient,

    @Named(SIGN_IN_REQUEST)
    private var signInRequest: BeginSignInRequest,
    @Named(SIGN_UP_REQUEST)
    private var signUpRequest: BeginSignInRequest,
) : AuthRepository {

    override suspend fun checkKakaoLogin(): Boolean {
        return kakaoLoginDataSource.isUserLoggedIn()
    }

    override suspend fun loginWithKakao(): ApiResponse<String> {
        return kakaoLoginDataSource.login()
            .onFailure {
                Logger.e(message())
            }
    }

    override suspend fun signInWithGoogle(idToken: String): ApiResponse<String> {
        return googleAuthService.googleSignIn(GoogleLogInRequest(idToken))
            .mapSuccess {
                accessToken
            }.onFailure {
                Logger.e(message())
            }
    }
    override fun getAuthState(viewModelScope: CoroutineScope) = callbackFlow {
        val authStateListener = AuthStateListener { auth ->
            auth.currentUser?.let { user ->
                /*try {
                    userRepository.getUserDocument(user)
                }
                catch (e: FirestoreException) {
                    if (e.message == "DocumentDoesNotExist") {
                        Log.i(TAG, "User Document Does Not Exist!")
                        GlobalScope.launch(Dispatchers.IO) {
                            verifyAuthTokenResult()
                        }
                    }
                }
                catch(e: Exception) {
                    // other errors
                }*/
            }

            trySend(auth.currentUser)
            Logger.d("User: ${auth.currentUser?.uid ?: "Not authenticated"}")
        }
        auth.addAuthStateListener(authStateListener)
        awaitClose {
            auth.removeAuthStateListener(authStateListener)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), auth.currentUser)

    override suspend fun verifyGoogleSignIn(): Boolean {
        auth.currentUser?.let { user ->
            if (user.providerData.map { it.providerId }.contains("google.com")) {
                return try {
                    googleSignInClient.silentSignIn().await()
                    true
                } catch (e: ApiException) {
                    Logger.e("Error: ${e.message}")
                    signOut()
                    false
                }
            }
        }
        return false
    }

    override suspend fun signInAnonymously(): FirebaseSignInResponse {
        return try {
            val authResult = auth.signInAnonymously().await()
            authResult?.user?.let { user ->
                Logger.i("FirebaseAuthSuccess: Anonymous UID: ${user.uid}")
            }
            Response.Success(authResult)
        } catch (error: Exception) {
            Logger.e("FirebaseAuthError: Failed to Sign in anonymously")
            Response.Failure(error)
        }
    }

    override suspend fun onTapSignIn(): OneTapSignInResponse {
        return try {
            val signInResult = oneTapClient.beginSignIn(signInRequest).await()
            Response.Success(signInResult)
        } catch (e: Exception) {
            try {
                val signUpResult = oneTapClient.beginSignIn(signUpRequest).await()
                Response.Success(signUpResult)
            } catch(e: Exception) {
                Response.Failure(e)
            }
        }
    }

    override suspend fun signInWithGoogle(credential: SignInCredential): FirebaseSignInResponse {
        val googleCredential = GoogleAuthProvider
            .getCredential(credential.googleIdToken, null)
        return authenticateUser(googleCredential)
    }

    private suspend fun authenticateUser(credential: AuthCredential): FirebaseSignInResponse {
        return if (auth.currentUser != null) {
            authLink(credential)
        } else {
            authSignIn(credential)
        }
    }

    private suspend fun authSignIn(credential: AuthCredential): FirebaseSignInResponse {
        return try {
            val authResult = auth.signInWithCredential(credential).await()
            Logger.i( "User authSignIn: ${authResult?.user?.uid}")
            DataProvider.updateAuthState(authResult?.user)
            Response.Success(authResult)
        }
        catch (error: Exception) {
            Response.Failure(error)
        }
    }

    private suspend fun authLink(credential: AuthCredential): FirebaseSignInResponse {
        return try {
            val authResult = auth.currentUser?.linkWithCredential(credential)?.await()
            Logger.i("User authLink: ${authResult?.user?.uid}")
            DataProvider.updateAuthState(authResult?.user)
            Response.Success(authResult)
        }
        catch (error: FirebaseAuthException) {
            when (error.errorCode) {
                "ERROR_CREDENTIAL_ALREADY_IN_USE",
                "ERROR_EMAIL_ALREADY_IN_USE" -> {
                    Logger.e( "FirebaseAuthError: authLink(credential:) failed, ${error.message}")
                    return authSignIn(credential)
                }
            }
            Response.Failure(error)
        }
        catch (error: Exception) {
            Response.Failure(error)
        }
    }


    override suspend fun signOut(): SignOutResponse {
        return try {
            oneTapClient.signOut().await()
            auth.signOut()
            Response.Success(true)
        }
        catch (e: java.lang.Exception) {
            Response.Failure(e)
        }
    }

    override fun checkNeedsReAuth(): Boolean {
        auth.currentUser?.metadata?.lastSignInTimestamp?.let { lastSignInDate ->
            return !Date(lastSignInDate).isWithinPast(5)
        }
        return false
    }

    override suspend fun authorizeGoogleSignIn(): String? {
        auth.currentUser?.let { user ->
            if (user.providerData.map { it.providerId }.contains("google.com")) {
                try {
                    val account = googleSignInClient.silentSignIn().await()
                    return account.idToken
                } catch (e: ApiException) {
                    Logger.e("Error: ${e.message}")
                }
            }
        }
        return null
    }

    private suspend fun reauthenticate(googleIdToken: String) {
        val googleCredential = GoogleAuthProvider
            .getCredential(googleIdToken, null)
        auth.currentUser?.reauthenticate(googleCredential)?.await()
    }

    override suspend fun deleteUserAccount(googleIdToken: String?): DeleteAccountResponse {
        return try {
            auth.currentUser?.let { user ->
                if (user.providerData.map { it.providerId }.contains("google.com")) {
                    // Re-authenticate if needed
                    if (checkNeedsReAuth() && googleIdToken != null) {
                        reauthenticate(googleIdToken)
                    }
                    // Revoke
                    googleSignInClient.revokeAccess().await()
                    oneTapClient.signOut().await()
                }
                // Delete firebase user
                auth.currentUser?.delete()?.await()
                Response.Success(true)
            }
            Logger.e("FirebaseAuthError: Current user is not available")
            Response.Success(false)
        }
        catch (e: Exception) {
            Logger.e( "FirebaseAuthError: Failed to delete user")
            Response.Failure(e)
        }
    }

    private suspend fun verifyAuthTokenResult(): Boolean {
        return try {
            auth.currentUser?.getIdToken(true)?.await()
            true
        } catch (e: Exception) {
            Logger.i("Error retrieving id token result. $e")
            false
        }
    }
}