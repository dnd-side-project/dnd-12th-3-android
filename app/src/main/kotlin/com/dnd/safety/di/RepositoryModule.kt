package com.dnd.safety.di

import com.dnd.safety.data.local.dao.SampleDao
import com.dnd.safety.data.remote.api.SampleApi
import com.dnd.safety.data.repository.SampleRepositoryImpl
import com.dnd.safety.domain.repository.SampleRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideSampleRepository(
        api: SampleApi,
        dao: SampleDao,
    ): SampleRepository = SampleRepositoryImpl(api, dao)
}