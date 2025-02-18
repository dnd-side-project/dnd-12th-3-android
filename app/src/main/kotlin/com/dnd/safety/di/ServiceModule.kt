package com.dnd.safety.di

import com.dnd.safety.data.remote.api.CommentService
import com.dnd.safety.data.remote.api.LoginService
import com.dnd.safety.data.remote.api.IncidentListService
import com.dnd.safety.data.remote.api.IncidentService
import com.dnd.safety.data.remote.api.LawDistrictService
import com.dnd.safety.data.remote.api.LocationService
import com.dnd.safety.data.remote.api.MyReportService
import com.dnd.safety.data.remote.api.MyTownService
import com.dnd.safety.di.utils.GoogleRetrofit
import com.dnd.safety.di.utils.KakaoRetrofit
import com.dnd.safety.di.utils.LawRetrofit
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {

    @Provides
    @Singleton
    fun provideLocationService(
        @KakaoRetrofit retrofit: Retrofit
    ): LocationService {
        return retrofit.create(LocationService::class.java)
    }

    @Provides
    @Singleton
    fun provideIncidentService(
        retrofit: Retrofit
    ): IncidentService {
        return retrofit.create(IncidentService::class.java)
    }

    @Provides
    @Singleton
    fun provideLawDistrictService(
        @LawRetrofit retrofit: Retrofit
    ): LawDistrictService {
        return retrofit.create(LawDistrictService::class.java)
    }

    @Provides
    @Singleton
    fun provideIncidentListService(
        retrofit: Retrofit
    ): IncidentListService = retrofit.create(IncidentListService::class.java)

    @Provides
    @Singleton
    fun provideMyTownService(
        retrofit: Retrofit
    ): MyTownService = retrofit.create(MyTownService::class.java)

    @Provides
    @Singleton
    fun provideLoginService(
        retrofit: Retrofit
    ): LoginService {
        return retrofit.create(LoginService::class.java)
    }

    @Provides
    @Singleton
    fun provideMyReportService(
        retrofit: Retrofit
    ): MyReportService {
        return retrofit.create(MyReportService::class.java)
    }

    @Provides
    @Singleton
    fun provideCommentService(
        retrofit: Retrofit
    ): CommentService {
        return retrofit.create(CommentService::class.java)
    }
}