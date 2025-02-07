package com.dnd.safety

import android.app.Application
import com.dnd.safety.BuildConfig.MAPS_API_KEY
import com.google.android.libraries.places.api.Places
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App: Application() {
    override fun onCreate() {
        super.onCreate()
        KakaoSdk.init(this, BuildConfig.KAKAO_NATIVE_APP_KEY)
        Places.initializeWithNewPlacesApiEnabled(this, MAPS_API_KEY)
    }
}