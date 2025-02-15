package com.dnd.safety.di

import android.app.Application
import android.content.Context
import com.dnd.safety.BuildConfig
import com.dnd.safety.data.repository.AuthRepositoryImpl
import com.dnd.safety.domain.repository.AuthRepository
import com.dnd.safety.presentation.navigation.utils.GoogleSignInHelper
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    const val SIGN_IN_REQUEST = "SIGN_IN_REQUEST"
    const val SIGN_UP_REQUEST = "SIGN_UP_REQUEST"

    @Provides
    @Singleton
    fun provideGoogleSignInHelper(
        @ApplicationContext context: Context
    ): GoogleSignInHelper {
        return GoogleSignInHelper(context)
    }

    @Provides
    @Singleton
    fun provideFirebaseAuth() = FirebaseAuth.getInstance()

    @Provides
    fun provideOneTapClient(
        @ApplicationContext
        context: Context
    ) = Identity.getSignInClient(context)

    @Provides
    fun provideGoogleSignInOptions(
    ) = GoogleSignInOptions
        .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(BuildConfig.GOOGLE_CLIENT_ID)
        .requestEmail()
        .build()

    @Provides
    fun provideGoogleSignInClient(
        app: Application,
        options: GoogleSignInOptions
    ) = GoogleSignIn.getClient(app, options)

    @Provides
    @Named(SIGN_IN_REQUEST)
    fun provideSignInRequest(
    ) = BeginSignInRequest.Builder()
        .setGoogleIdTokenRequestOptions(
            BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                .setSupported(true)
                .setServerClientId(BuildConfig.GOOGLE_CLIENT_ID)
                .setFilterByAuthorizedAccounts(true)
                .build()
        )
        .setAutoSelectEnabled(true)
        .build()

    @Provides
    @Named(SIGN_UP_REQUEST)
    fun provideSignUpRequest(
    ) = BeginSignInRequest.Builder()
        .setGoogleIdTokenRequestOptions(
            BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                .setSupported(true)
                .setServerClientId(BuildConfig.GOOGLE_CLIENT_ID)
                .setFilterByAuthorizedAccounts(false)
                .build()
        )
        .build()
}