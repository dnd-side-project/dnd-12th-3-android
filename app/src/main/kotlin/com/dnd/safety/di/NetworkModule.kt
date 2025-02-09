package com.dnd.safety.di

import com.dnd.safety.data.remote.api.IncidentsApi
import com.dnd.safety.BuildConfig
import com.dnd.safety.data.remote.api.GoogleAuthService
import com.dnd.safety.data.remote.api.LocationService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.skydoves.sandwich.retrofit.adapters.ApiResponseCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
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
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
        encodeDefaults = true
    }

    @Provides
    @Singleton
    fun provideConverterFactory(json: Json): Converter.Factory = json.asConverterFactory("application/json".toMediaType())

    @Provides
    @Singleton
    fun provideRetrofit(
        @Named("baseOkHttpClient") okHttpClient: OkHttpClient,
        converterFactory: Converter.Factory
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl("http://3.37.245.234:8080")
            .client(okHttpClient)
            .addConverterFactory(converterFactory)
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
        converterFactory: Converter.Factory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://dapi.kakao.com/")
            .addConverterFactory(converterFactory)
            .addCallAdapterFactory(ApiResponseCallAdapterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Named("googleRetrofit")
    fun provideGoogleRetrofit(
        @Named("baseOkHttpClient") okHttpClient: OkHttpClient,
        converterFactory: Converter.Factory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080/")
            .addConverterFactory(converterFactory)
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
    fun provideIncidentsApi(retrofit: Retrofit): IncidentsApi =
        retrofit.create(IncidentsApi::class.java)

}
