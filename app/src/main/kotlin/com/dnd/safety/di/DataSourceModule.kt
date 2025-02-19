package com.dnd.safety.di

import android.content.Context
import android.location.LocationManager
import com.dnd.safety.data.remote.datasource.KakaoLoginDataSourceImpl
import com.dnd.safety.domain.datasource.KakaoLoginDataSource
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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


    companion object {
        @Provides
        @Singleton
        fun provideLocationManager(
            @ApplicationContext context: Context
        ): LocationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }
}