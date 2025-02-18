package com.dnd.safety.di.utils

import jakarta.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class KakaoRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class GoogleRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class LawRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class SignInRequest

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class SignUpRequest