package com.dnd.safety.di

import com.dnd.safety.BuildConfig
import com.dnd.safety.data.remote.api.GoogleAuthService
import com.dnd.safety.data.remote.api.IncidentService
import com.dnd.safety.data.remote.api.IncidentsApi
import com.dnd.safety.data.remote.api.LocationService
import com.google.gson.Gson
import com.skydoves.sandwich.retrofit.adapters.ApiResponseCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    @Named("baseOkHttpClient")
    fun provideBaseOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpNetworkLogger())
            .addInterceptor { chain ->
                val token = runBlocking { "" }

                val requestBuilder = chain.request().newBuilder()
                    .header("Content-Type", "application/json; charset=utf-8")
                    .header("token", token)
                val request = requestBuilder.build()
                chain.proceed(request)
            }
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build()
    }

    @Provides
    @Singleton
    fun provideGson(): Gson = Gson()

    @Provides
    @Singleton
    fun provideGsonConverterFactory(gson: Gson): GsonConverterFactory =
        GsonConverterFactory.create(gson)

    @Provides
    @Singleton
    fun provideRetrofit(
        @Named("baseOkHttpClient") okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl("http://3.37.245.234:8080")
            .client(okHttpClient)
            .addConverterFactory(gsonConverterFactory)
            .addCallAdapterFactory(ApiResponseCallAdapterFactory.create())
            .build()

    @Provides
    @Singleton
    @Named("kakaoOkHttpClient")
    fun provideKakaoOkHttpClient(
        @Named("baseOkHttpClient") okHttpClient: OkHttpClient
    ): OkHttpClient {
        return okHttpClient.newBuilder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Authorization", "KakaoAK ${BuildConfig.KAKAO_REST_API_KEY}")
                    .build()
                chain.proceed(request)
            }
            .build()
    }

    @Provides
    @Named("kakaoRetrofit")
    fun provideKakaoRetrofit(
        @Named("kakaoOkHttpClient") okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://dapi.kakao.com/")
            .addConverterFactory(gsonConverterFactory)
            .addCallAdapterFactory(ApiResponseCallAdapterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Named("googleRetrofit")
    fun provideGoogleRetrofit(
        @Named("baseOkHttpClient") okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080/")
            .addConverterFactory(gsonConverterFactory)
            .addCallAdapterFactory(ApiResponseCallAdapterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Named("baseRetrofit")
    fun provideBaseRetrofit(
        @Named("baseOkHttpClient") okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://3.37.245.234:8080/")
            .addConverterFactory(gsonConverterFactory)
            .addCallAdapterFactory(ApiResponseCallAdapterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideLocationService(
        @Named("kakaoRetrofit") retrofit: Retrofit
    ): LocationService {
        return retrofit.create(LocationService::class.java)
    }

    @Provides
    @Singleton
    fun provideAuthService(
        @Named("googleRetrofit") retrofit: Retrofit
    ): GoogleAuthService {
        return retrofit.create(GoogleAuthService::class.java)
    }

    @Provides
    @Singleton
    fun provideIncidentService(
        @Named("baseRetrofit") retrofit: Retrofit
    ): IncidentService {
        return retrofit.create(IncidentService::class.java)
    }

    @Provides
    @Singleton
    fun provideIncidentsApi(
        @Named("baseRetrofit") retrofit: Retrofit
    ): IncidentsApi =
        retrofit.create(IncidentsApi::class.java)
}