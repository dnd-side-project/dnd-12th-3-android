package com.dnd.safety.di

import com.dnd.safety.data.repository.SampleRepositoryImpl
import com.dnd.safety.domain.repository.SampleRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun provideSampleRepository(
        sampleRepositoryImpl: SampleRepositoryImpl
    ): SampleRepository
}