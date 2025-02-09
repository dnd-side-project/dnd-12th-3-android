package com.dnd.safety.di

import android.content.Context
import com.dnd.safety.presentation.navigation.utils.GoogleSignInHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {
    @Provides
    @Singleton
    fun provideGoogleSignInHelper(
        @ApplicationContext context: Context
    ): GoogleSignInHelper {
        return GoogleSignInHelper(context)
    }

}