package com.dnd.safety

import android.app.Application
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import com.dnd.safety.BuildConfig.GOOGLE_MAPS_API_KEY
import com.google.android.libraries.places.api.Places
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        KakaoSdk.init(this, BuildConfig.KAKAO_NATIVE_APP_KEY)
        Places.initializeWithNewPlacesApiEnabled(this, GOOGLE_MAPS_API_KEY)

        fcmSetting()
    }

    private fun fcmSetting() {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelMessage = NotificationChannel(
            resources.getString(R.string.fcm_topic),
            resources.getString(R.string.app_name),
            NotificationManager.IMPORTANCE_HIGH
        )

        channelMessage.description = resources.getString(R.string.fcm_description)
        channelMessage.enableLights(true)
        channelMessage.lightColor = Color.GREEN
        channelMessage.enableVibration(true)
        channelMessage.vibrationPattern = longArrayOf(100, 200, 100, 200)
        channelMessage.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        notificationManager.createNotificationChannel(channelMessage)
    }
}