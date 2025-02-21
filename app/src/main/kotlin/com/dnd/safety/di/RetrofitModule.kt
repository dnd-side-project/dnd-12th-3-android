package com.dnd.safety.di

import com.dnd.safety.BuildConfig
import com.dnd.safety.data.local.datastore.datasource.UserPreferenceDataSource
import com.dnd.safety.di.utils.GoogleRetrofit
import com.dnd.safety.di.utils.HttpNetworkLogger
import com.dnd.safety.di.utils.KakaoRetrofit
import com.dnd.safety.di.utils.LawRetrofit
import com.dnd.safety.utils.Const
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
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

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
    fun provideOkHttpClientBuilder(
        userPreferenceDataSource: UserPreferenceDataSource
    ): OkHttpClient.Builder {
        return OkHttpClient.Builder()
            .addInterceptor(HttpNetworkLogger())
            .addInterceptor { chain ->
                val token = runBlocking { userPreferenceDataSource.getToken() }

                val requestBuilder = chain.request().newBuilder()
                    .header("Content-Type", "application/json; charset=utf-8")
                    .also {
                        if (token.isNotBlank()) {
                            it.header("Authorization", "Bearer $token")
                        }
                    }
                val request = requestBuilder.build()
                chain.proceed(request)
            }
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        builder: OkHttpClient.Builder
    ): OkHttpClient {
        return builder
            .addInterceptor { chain ->
                val token = runBlocking { "" }

                val requestBuilder = chain.request().newBuilder()
                    .header("Content-Type", "application/json; charset=utf-8")
                    .header("token", token)
                val request = requestBuilder.build()
                chain.proceed(request)
            }
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        converterFactory: Converter.Factory
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(Const.HOST_URL)
            .client(okHttpClient)
            .addConverterFactory(converterFactory)
            .addCallAdapterFactory(ApiResponseCallAdapterFactory.create())
            .build()

    @Provides
    @Singleton
    @KakaoRetrofit
    fun provideKakaoRetrofit(
        builder: OkHttpClient.Builder,
        converterFactory: Converter.Factory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://dapi.kakao.com/")
            .addConverterFactory(converterFactory)
            .addCallAdapterFactory(ApiResponseCallAdapterFactory.create())
            .client(builder
                .addInterceptor { chain ->
                    val request = chain.request().newBuilder()
                        .addHeader("Authorization", "KakaoAK ${BuildConfig.KAKAO_REST_API_KEY}")
                        .build()
                    chain.proceed(request)
                }
                .build())
            .build()
    }

    @Provides
    @Singleton
    @GoogleRetrofit
    fun provideGoogleRetrofit(
        builder: OkHttpClient.Builder,
        converterFactory: Converter.Factory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080/")
            .addConverterFactory(converterFactory)
            .addCallAdapterFactory(ApiResponseCallAdapterFactory.create())
            .client(builder.build())
            .build()
    }

    @Provides
    @Singleton
    @LawRetrofit
    fun provideLawRetrofit(
        builder: OkHttpClient.Builder,
        converterFactory: Converter.Factory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://business.juso.go.kr/addrlink/")
            .addConverterFactory(converterFactory)
            .addCallAdapterFactory(ApiResponseCallAdapterFactory.create())
            .client(builder.build())
            .build()
    }
}

