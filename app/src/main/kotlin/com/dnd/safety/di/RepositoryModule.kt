package com.dnd.safety.di

import dagger.Binds
import com.dnd.safety.data.repository.AuthRepositoryImpl
import com.dnd.safety.data.repository.LocationRepositoryImpl
import com.dnd.safety.domain.repository.AuthRepository
import com.dnd.safety.domain.repository.LocationRepository
import com.dnd.safety.data.repository.IncidentsRepositoryImpl
import com.dnd.safety.domain.repository.IncidentsRepository
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindLocationRepository(
        locationRepositoryImpl: LocationRepositoryImpl
    ): LocationRepository


    @Binds
    @Singleton
    abstract fun provideIncidentsRepository(
        repositoryImpl: IncidentsRepositoryImpl
    ): IncidentsRepository
}