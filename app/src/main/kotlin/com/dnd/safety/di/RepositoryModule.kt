package com.dnd.safety.di

import android.content.ContentResolver
import android.content.Context
import com.dnd.safety.data.repository.MediaRepositoryImpl
import com.dnd.safety.data.repository.SampleRepositoryImpl
import com.dnd.safety.domain.repository.MediaRepository
import com.dnd.safety.domain.repository.SampleRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun provideSampleRepository(
        sampleRepositoryImpl: SampleRepositoryImpl
    ): SampleRepository


    @Binds
    abstract fun bindMediaRepository(
        mediaRepositoryImpl: MediaRepositoryImpl
    ): MediaRepository

}