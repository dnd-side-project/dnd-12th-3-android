package com.dnd.safety.di

import android.content.ContentResolver
import android.content.Context
import com.dnd.safety.data.datasorce.MediaPagingSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PagingModule {

    @Provides
    @Singleton
    fun provideContentResolver(
        @ApplicationContext context: Context
    ): ContentResolver = context.contentResolver

    @Provides
    @Singleton
    fun provideMediaPagingSource(
        contentResolver: ContentResolver
    ): MediaPagingSource = MediaPagingSource(contentResolver)
}