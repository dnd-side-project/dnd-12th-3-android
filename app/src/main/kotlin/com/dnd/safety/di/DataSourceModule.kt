package com.dnd.safety.di

import com.dnd.safety.data.remote.datasource.KakaoLoginDataSourceImpl
import com.dnd.safety.domain.datasource.KakaoLoginDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {
    @Binds
    @Singleton
    abstract fun bindKakaoLoginDataSource(
        kakaoLoginDataSourceImpl: KakaoLoginDataSourceImpl
    ): KakaoLoginDataSource

}